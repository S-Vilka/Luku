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
//                 git branch: 'jenkins', url: 'git@github.com:S-Vilka/Luku.git'
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

pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        MAVEN_HOME = '/opt/homebrew/Cellar/maven/3.9.9/libexec'
        PATH = "${MAVEN_HOME}/bin:/usr/local/bin:${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKERHUB_REPO = 'mahnoor95/luku'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                git branch: 'jenkins', url: 'git@github.com:S-Vilka/Luku.git'
            }
        }



       stage('Verify Docker') {
            steps {
                sh 'docker --version'  // Check if Docker is available
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build Docker image
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                // Push Docker image to Docker Hub
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}