# gs-rest-service
Building a RESTful Web Service :: Learn how to create a RESTful web service with Spring.

Connect to the cluster and Build with: 

oc new-app image-registry.openshift-image-registry.svc:5000/openshift/jboss-webserver56-openjdk8-tomcat9-openshift-ubi8~https://github.com/dandan2000/gs-rest-service.git

Or

    
    kind: BuildConfig
    apiVersion: build.openshift.io/v1
    metadata:
      annotations:
        openshift.io/generated-by: OpenShiftNewApp
      name: gs-rest-service
      labels:
        app: gs-rest-service
        app.kubernetes.io/component: gs-rest-service
        app.kubernetes.io/instance: gs-rest-service
    spec:
      nodeSelector: null
      output:
        to:
          kind: ImageStreamTag
          name: 'gs-rest-service:latest'
      resources: {}
      successfulBuildsHistoryLimit: 5
      failedBuildsHistoryLimit: 5
      strategy:
        type: Source
        sourceStrategy:
          from:
            kind: ImageStreamTag
            name: 'jboss-webserver56-openjdk8-tomcat9-openshift-ubi8:latest'
            namespace: openshift
      postCommit: {}
      source:
        type: Git
        git:
          uri: 'https://github.com/dandan2000/gs-rest-service.git'
      triggers:
        - type: ConfigChange
        - type: ImageChange
          imageChange: {}
      runPolicy: Serial

Tomcat 8 Java 8
Call it with
http://localhost:8080/rest-service-complete/greeting
