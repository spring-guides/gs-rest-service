pipeline {
    agent any

    environment {
        PROJECT_ID        = 'DeploymentTest'             // your GCP project
        REGION            = 'europe-west1'
        ZONE              = 'europe-west1-b'
        CLUSTER_NAME      = 'gs-rest-cluster'
        ARTIFACT_REPO     = 'gs-rest-repo'
        IMAGE_NAME        = 'my-image:latest'
        IMAGE_TAG         = "v${env.BUILD_NUMBER}"
        IMAGE_FULL        = "${REGION}-docker.pkg.dev/${PROJECT_ID}/${ARTIFACT_REPO}/${IMAGE_NAME}:${IMAGE_TAG}"
        K8S_NAMESPACE     = 'gs-rest'
        GOOGLE_APPLICATION_CREDENTIALS = credentials('gcp-service-account-json')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Auth to GCP') {
            steps {
                sh '''
                  gcloud auth activate-service-account --key-file="${GOOGLE_APPLICATION_CREDENTIALS}"
                  gcloud config set project ${PROJECT_ID}
                  gcloud config set compute/zone ${ZONE}
                '''
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                sh '''
                  gcloud auth configure-docker ${REGION}-docker.pkg.dev -q
                  docker build -t ${IMAGE_FULL} .
                  docker push ${IMAGE_FULL}
                '''
            }
        }

        stage('Get GKE Credentials') {
            steps {
                sh '''
                  gcloud container clusters get-credentials ${CLUSTER_NAME} --zone ${ZONE} --project ${PROJECT_ID}
                '''
            }
        }

        stage('Deploy to GKE') {
            steps {
                sh '''
                  kubectl apply -f kubernetes/namespace.yaml

                  # Substitute image into deployment and apply
                  sed "s|REPLACE_WITH_IMAGE|${IMAGE_FULL}|g" kubernetes/deployment.yaml | kubectl apply -f -

                  kubectl apply -f kubernetes/service.yaml
                '''
            }
        }
    }

    post {
        success {
            echo "Deployment successful: ${IMAGE_FULL}"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
