pipeline {
    agent any

    tools {
        maven 'M3.3.9'
    }

    triggers {
        pollSCM('*/1 * * * *')
    }

    stages {
        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('build') {
            steps {
                withMaven() {
                    sh 'mvn -e -DincludeSrcJavadocs clean source:jar install'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '*.war', onlyIfSuccessful: true
        }
        failure {
            // notify users when the Pipeline fails
            mail to: 'willer@lundogbendsen.dk',
                    subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                    body: "Something is wrong with ${env.BUILD_URL}"
        }
    }
}
