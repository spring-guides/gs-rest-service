pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                dockerNode(dockerHost: 'unix:///var/run/docker.sock', image: 'maven:3.6-jdk-8-alpine') {
                  sh 'mvn package'
                }
            }
        }
    }
}