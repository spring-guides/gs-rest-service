# gs-rest-service
Building a RESTful Web Service :: Learn how to create a RESTful web service with Spring.

Connect to the cluster and Build with: 

oc new-app image-registry.openshift-image-registry.svc:5000/openshift/jboss-webserver56-openjdk8-tomcat9-openshift-ubi8~https://github.com/dandan2000/gs-rest-service.git

Tomcat 8 Java 8
Call it with
http://localhost:8080/rest-service-complete/greeting
