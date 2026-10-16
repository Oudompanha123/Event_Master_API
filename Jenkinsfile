// Runs a Maven goal inside a throwaway container.
//
// The Jenkins controller is itself a container (see devops/ in the portfolio repo):
// it ships JDK 17 and no Maven at all, while this project needs 21. Rather than
// rebuilding the shared controller image, every Maven step borrows the official
// maven:*-temurin-21 image through the Docker socket the controller already mounts.
//
// The flags are all load-bearing:
//   -v $JENKINS_VOLUME       the workspace lives inside the jenkins_home volume, so
//                            mounting it makes $WORKSPACE name the same files on
//                            both sides of the socket.
//   --user 1000:1000         everything Maven writes stays owned by the jenkins
//                            user, or the next checkout/cleanup fails on root files.
//   --group-add 0            what actually grants access to the mounted socket,
//                            which is root:root 0660 (same trick as group_add in
//                            the controller's compose file).
//   -v docker.sock           Testcontainers starts its PostgreSQL on the host daemon.
//   TESTCONTAINERS_HOST_OVERRIDE  tells Testcontainers to reach that database at
//                            host.docker.internal:<published port> - the default of
//                            'localhost' is this container, not the Docker host.
//   --network cicd           puts the build on the same network as SonarQube, so
//                            http://sonarqube:9000 resolves during the scan.
def mvn(String goals) {
    withEnv(["MVN_GOALS=${goals}"]) {
        sh '''
            # sonar-scanner-maven 5.x authenticates from SONAR_TOKEN. withSonarQubeEnv
            # exports the same secret as SONAR_AUTH_TOKEN too, so accept either and
            # forward it under the one name the scanner actually reads. Empty outside
            # the SonarQube stages, where no Maven goal looks at it.
            export SONAR_TOKEN="${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}"

            docker run --rm \
              --user 1000:1000 \
              --group-add 0 \
              --network "$CICD_NETWORK" \
              --add-host host.docker.internal:host-gateway \
              -v "$JENKINS_VOLUME":/var/jenkins_home \
              -v /var/run/docker.sock:/var/run/docker.sock \
              -e MAVEN_CONFIG=/var/jenkins_home/.m2 \
              -e TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal \
              -e SONAR_HOST_URL -e SONAR_TOKEN \
              -w "$WORKSPACE" \
              "$MAVEN_IMAGE" \
              mvn -B -Duser.home=/var/jenkins_home $MVN_GOALS
        '''
    }
}

/**
 * True when this build is of the production branch.
 *
 * <p>GitFlow puts releasable code on main and day-to-day integration on develop,
 * so only main is allowed to build an image and deploy it. BRANCH_NAME is set
 * only by multibranch jobs; a plain pipeline job gets GIT_BRANCH from the git
 * plugin in the form 'origin/main', so both are accepted.
 */
def isProductionBranch() {
    String branch = env.BRANCH_NAME ?: env.GIT_BRANCH ?: ''
    return branch == 'main' || branch.endsWith('/main')
}

/** main and develop are analysed; short-lived feature branches only run tests. */
def isAnalysedBranch() {
    String branch = env.BRANCH_NAME ?: env.GIT_BRANCH ?: ''
    return isProductionBranch() || branch == 'develop' || branch.endsWith('/develop')
}

pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 40, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Two builds deploying to the same container name would fight each other.
        disableConcurrentBuilds()
    }

    // Jenkins asks GitHub for new commits every ~5 minutes and builds only
    // when the revision changed. The H spreads the poll across the hour so
    // every job on this controller doesn't hit SCM at the same second.
    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        IMAGE_NAME     = 'event-master'
        HOST_PORT      = '8899'
        // Deployed as a Swarm stack, so the unit of deployment is a service
        // rather than a container: 'docker stack deploy' names the service
        // <stack>_<service in stack.yml>.
        STACK_NAME     = 'event-master'
        SERVICE_NAME   = 'event-master_api'
        // Named volume rather than a C:\ bind mount: the deploy runs from a Linux
        // container, which cannot hand a Windows path to the daemon.
        DATA_VOLUME    = 'event-master-data'
        // The standalone container the pre-Swarm pipeline left behind; it holds
        // the same published port, so it has to go before the service can bind it.
        LEGACY_CONTAINER = 'event-master-meta'

        MAVEN_IMAGE    = 'maven:3.9-eclipse-temurin-21'
        // Docker volume and network created by the CI stack's compose project ('cicd').
        JENKINS_VOLUME = 'cicd_jenkins_home'
        CICD_NETWORK   = 'cicd'

        // Name of the SonarQube server configured under Manage Jenkins > System.
        SONARQUBE_ENV  = 'SonarQube'

        DB_URL      = 'jdbc:postgresql://host.docker.internal:5432/postgres'
        DB_USERNAME = 'postgres'
    }

    stages {
        // No Checkout stage: Jenkins already cloned the repo to read this file.

        stage('Test') {
            steps {
                // Also leaves target/classes and target/site/jacoco/jacoco.xml behind,
                // both of which the SonarQube stage needs.
                mvn 'test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('SonarQube Analysis') {
            when { expression { isAnalysedBranch() } }
            steps {
                // Injects the server URL and token from the SonarQube server
                // configuration; mvn() forwards both into the build container.
                withSonarQubeEnv(env.SONARQUBE_ENV) {
                    // Interpolated by Groovy, not the shell: $MVN_GOALS is only
                    // expanded once, so a $VAR inside it would reach Maven verbatim.
                    mvn "sonar:sonar -Dsonar.projectVersion=${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Quality Gate') {
            when { expression { isAnalysedBranch() } }
            steps {
                // The waitForQualityGate step needs SonarQube to call a webhook back
                // into Jenkins, which this setup deliberately does not configure, so
                // the gate is polled instead: wait for the Compute Engine to finish
                // processing the report, then ask for that analysis's gate status.
                withSonarQubeEnv(env.SONARQUBE_ENV) {
                    timeout(time: 5, unit: 'MINUTES') {
                        sh '''
                            set -e
                            TOKEN="${SONAR_TOKEN:-$SONAR_AUTH_TOKEN}"

                            # The scanner drops this under target/sonar; located rather
                            # than hard-coded so the gate does not break if a future
                            # scanner release moves its working directory.
                            REPORT_TASK=$(find target -name report-task.txt -print -quit)
                            if [ -z "$REPORT_TASK" ]; then
                                echo "No report-task.txt found - did the analysis stage upload a report?"
                                exit 1
                            fi
                            CE_TASK_URL=$(grep '^ceTaskUrl=' "$REPORT_TASK" | cut -d= -f2-)

                            STATUS=PENDING
                            while [ "$STATUS" = PENDING ] || [ "$STATUS" = IN_PROGRESS ]; do
                                sleep 5
                                RESP=$(curl -s -u "$TOKEN:" "$CE_TASK_URL")
                                STATUS=$(echo "$RESP" | jq -r .task.status)
                                echo "SonarQube analysis task status: $STATUS"
                            done

                            if [ "$STATUS" != SUCCESS ]; then
                                echo "$RESP"
                                exit 1
                            fi

                            ANALYSIS_ID=$(echo "$RESP" | jq -r .task.analysisId)
                            QG=$(curl -s -u "$TOKEN:" \
                                "$SONAR_HOST_URL/api/qualitygates/project_status?analysisId=$ANALYSIS_ID")
                            QG_STATUS=$(echo "$QG" | jq -r .projectStatus.status)
                            echo "Quality Gate status: $QG_STATUS"

                            if [ "$QG_STATUS" != OK ]; then
                                echo "$QG" | jq .
                                exit 1
                            fi
                        '''
                    }
                }
            }
        }

        stage('Docker Build') {
            when { expression { isProductionBranch() } }
            steps {
                sh 'docker build -t "$IMAGE_NAME:$BUILD_NUMBER" -t "$IMAGE_NAME:latest" .'
            }
        }

        stage('Deploy') {
            when { expression { isProductionBranch() } }
            steps {
                withCredentials([
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'mail-password', variable: 'MAIL_PASSWORD'),
                    string(credentialsId: 'jwt-secret', variable: 'JWT_SECRET')
                ]) {
                    // stack.yml reads every value through ${...}, which
                    // 'docker stack deploy' substitutes from this shell, so the
                    // credentials reach the service without being written to disk.
                    sh '''
                        # The pre-Swarm pipeline left a standalone container holding
                        # the same published port. Removing it is a one-time
                        # migration, and a no-op safety net from then on.
                        docker rm -f "$LEGACY_CONTAINER" 2>/dev/null || true

                        # Declared external in stack.yml so uploads survive a
                        # 'stack rm'; creating it is idempotent.
                        docker volume create "$DATA_VOLUME" >/dev/null

                        IMAGE_TAG="$BUILD_NUMBER" docker stack deploy \
                          --compose-file stack.yml \
                          --prune \
                          "$STACK_NAME"
                    '''
                }
            }
        }

        stage('Health Check') {
            when { expression { isProductionBranch() } }
            steps {
                // Asks the running app over its published port instead of grepping
                // 'docker service logs'. With update_config order start-first the
                // previous task lingers for a few seconds and its own
                // "Started ... in" line is still in the service log, so matching
                // on that would pass before the new task is even serving.
                sh '''
                    i=0
                    while [ $i -lt 40 ]; do
                        i=$((i + 1))
                        sleep 3

                        REPLICAS=$(docker service ls --filter name="$SERVICE_NAME" --format '{{.Replicas}}')
                        [ "$REPLICAS" = "1/1" ] || continue

                        CODE=$(curl -s -o /dev/null -w '%{http_code}' --max-time 10 \
                            "http://host.docker.internal:$HOST_PORT/swagger-ui/index.html" || true)
                        if [ "$CODE" = "200" ]; then
                            echo "App is up on http://localhost:$HOST_PORT (replicas $REPLICAS)"
                            exit 0
                        fi
                    done

                    echo "Service did not become healthy. Task state:"
                    docker service ps "$SERVICE_NAME" --no-trunc 2>&1 || true
                    echo "Last log lines:"
                    docker service logs --tail 40 "$SERVICE_NAME" 2>&1 || true
                    exit 1
                '''
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f'
        }
    }
}
