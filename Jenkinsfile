pipeline {
    agent any

    environment {
        MAVEN_HOME = '/opt/homebrew/Cellar/maven/3.9.9/libexec'
        PATH = "${MAVEN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                git branch: 'main', url: 'git@github.com:S-Vilka/Luku.git'
            }
        }

        stage('Build and Package') {
            steps {
                // Build and package the project using Maven
                sh 'mvn clean package'
            }
        }


        stage('Code Coverage') {
            steps {
                // Generate JaCoCo code coverage report
                sh 'mvn jacoco:report'
            }
        }
    }

    post {
        always {
            // Archive the build artifacts
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            // Publish JUnit test results
            junit '**/target/surefire-reports/*.xml'
            // Archive the JaCoCo code coverage report
            jacoco execPattern: '**/target/jacoco.exec'
        }
    }
}