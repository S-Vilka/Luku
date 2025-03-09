// pipeline {
//     agent any
//
//     environment {
//         MAVEN_HOME = '/opt/homebrew/Cellar/maven/3.9.9/libexec'
//         PATH = "${MAVEN_HOME}/bin:${env.PATH}"
//     }
//
//     stages {
//         stage('Checkout') {
//             steps {
//                 // Checkout the code from the repository
//                 git branch: 'dockerNew', url: 'git@github.com:S-Vilka/Luku.git'
//             }
//         }
//
//         stage('Build and Package') {
//             steps {
//                 // Build and package the project using Maven
//                 sh 'mvn clean package'
//             }
//         }
//
//
//         stage('Code Coverage') {
//             steps {
//                 // Generate JaCoCo code coverage report
//                 sh 'mvn jacoco:report'
//             }
//         }
//     }
//
//     post {
//         always {
//             // Archive the build artifacts
//             archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
//             // Publish JUnit test results
//             junit '**/target/surefire-reports/*.xml'
//             // Archive the JaCoCo code coverage report
//             jacoco execPattern: '**/target/jacoco.exec'
//         }
//     }
// }
//


pipeline {
    agent any
    environment {
        MAVEN_HOME = '/opt/homebrew/Cellar/maven/3.9.9/libexec'
        PATH = "/opt/homebrew/bin:${MAVEN_HOME}/bin:${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'Docker_hub'
        DOCKERHUB_REPO = 'luku'
        DOCKER_IMAGE_TAG = 'latest_v1'
        DOCKERHUB_USER = 'mahnoor95'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'dockerNew', url: 'git@github.com:S-Vilka/Luku.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                sh 'mvn jacoco:report'
            }
        }
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Publish Coverage Report') {
            steps {
                jacoco()
            }
        }
//         stage('Build Docker Image') {
//             steps {
//                      script {
//                          def image = docker.build("${DOCKERHUB_USER}/${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
//                      }
//
//
//             }
//         }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                        // Log in to Docker Hub
                        sh "/usr/local/bin/docker login -u ${DOCKERHUB_USER} -p ${DOCKERHUB_PASSWORD}"

                         // Push Docker image to Docker Hub
                        sh "/usr/local/bin/docker push ${DOCKERHUB_USER}/${DOCKERHUB_REPO}"

//                         // Construct the full image tag
//                         def imageTag = "${DOCKERHUB_USER}/${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}"
//
//                         // Push Docker image to Docker Hub
//                         sh "/usr/local/bin/docker push ${imageTag}"
                    }
                }
            }
        }
    }
}
