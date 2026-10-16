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
        CONTAINER_NAME = 'event-master-meta'
        HOST_PORT      = '8899'
        // Named volume rather than a C:\ bind mount: the deploy runs from a Linux
        // container, which cannot hand a Windows path to the daemon.
        DATA_VOLUME    = 'event-master-data'

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
            steps {
                sh 'docker build -t "$IMAGE_NAME:$BUILD_NUMBER" -t "$IMAGE_NAME:latest" .'
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'mail-password', variable: 'MAIL_PASSWORD'),
                    string(credentialsId: 'jwt-secret', variable: 'JWT_SECRET')
                ]) {
                    sh '''
                        docker rm -f "$CONTAINER_NAME" 2>/dev/null || true
                        docker run -d --name "$CONTAINER_NAME" --restart always \
                          -p "$HOST_PORT":8080 \
                          --add-host host.docker.internal:host-gateway \
                          -e DB_URL -e DB_USERNAME -e DB_PASSWORD -e MAIL_PASSWORD -e JWT_SECRET \
                          -v "$DATA_VOLUME":/src/main/resources \
                          "$IMAGE_NAME:$BUILD_NUMBER"
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    i=0
                    while [ $i -lt 30 ]; do
                        i=$((i + 1))
                        sleep 3
                        [ "$(docker inspect -f '{{.State.Running}}' "$CONTAINER_NAME")" = true ] || break
                        if docker logs "$CONTAINER_NAME" 2>&1 | grep -q "Started .* in "; then
                            echo "App is up on http://localhost:$HOST_PORT"
                            exit 0
                        fi
                    done

                    echo "App did not start. Last log lines:"
                    docker logs --tail 40 "$CONTAINER_NAME" 2>&1 || true
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
