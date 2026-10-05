pipeline {
    agent any

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
        DB_URL         = 'jdbc:postgresql://host.docker.internal:5432/postgres'
        DB_USERNAME    = 'postgres'
    }

    stages {
        // No Checkout stage: Jenkins already cloned the repo to read this file.

        stage('Test') {
            steps {
                // mvnw.cmd only needs JAVA_HOME, which is set machine-wide, so no
                // Maven install is required on the agent. Testcontainers starts its
                // own PostgreSQL through the same Docker daemon this job uses.
                bat 'mvnw.cmd -B test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %IMAGE_NAME%:%BUILD_NUMBER% -t %IMAGE_NAME%:latest .'
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'db-password', variable: 'DB_PASSWORD'),
                    string(credentialsId: 'mail-password', variable: 'MAIL_PASSWORD')
                ]) {
                    bat '''
                        docker rm -f %CONTAINER_NAME% 2>nul
                        docker run -d --name %CONTAINER_NAME% --restart always ^
                          -p %HOST_PORT%:8080 ^
                          -e DB_URL -e DB_USERNAME -e DB_PASSWORD -e MAIL_PASSWORD ^
                          -v C:/deploy/event-master/data:/src/main/resources ^
                          %IMAGE_NAME%:%BUILD_NUMBER%
                    '''
                }
            }
        }

        stage('Health Check') {
            steps {
                powershell '''
                    for ($i = 0; $i -lt 30; $i++) {
                        Start-Sleep -Seconds 3
                        $running = docker inspect -f "{{.State.Running}}" $env:CONTAINER_NAME
                        if ($running -ne "true") { break }
                        if (docker logs $env:CONTAINER_NAME | Select-String "Started .* in ") {
                            Write-Host "App is up on http://localhost:$env:HOST_PORT"
                            exit 0
                        }
                    }
                    Write-Host "App did not start. Last log lines:"
                    docker logs --tail 40 $env:CONTAINER_NAME
                    exit 1
                '''
            }
        }
    }

    post {
        always {
            bat 'docker image prune -f'
        }
    }
}
