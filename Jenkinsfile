pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                // -f complete/pom.xml because the pom is inside the complete folder
                sh 'mvn -f complete/pom.xml clean package'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'complete/target/*.jar', fingerprint: true
            cleanWs()
        }
    }
}
