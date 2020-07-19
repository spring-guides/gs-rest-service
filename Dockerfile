# syntax=docker/dockerfile:experimental

FROM maven:3.6-jdk-8-alpine AS builder
WORKDIR /app
RUN --mount=target=. --mount=type=cache,target=/root/.m2 mvn package -DbuildDirectory=/target

FROM openjdk:8-jre-alpine
COPY --from=builder /target/rest-service-0.0.1-SNAPSHOT.jar /rest-service.jar
EXPOSE 8080
CMD ["java", "-jar", "/rest-service.jar"]
