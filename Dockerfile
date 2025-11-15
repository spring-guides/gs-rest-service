# build JAR inside docker
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

#Copy pom.xml first
COPY complete/pom.xml .

# Download dependencies
RUN mvn -f pom.xml dependency:go-offline

#Now copy the source code
COPY complete/src ./src

#Build the jar 
RUN mvn -f pom.xml clean package -DskipTests

# Runtime Image

FROM eclipse-temurin:17-jre

WORKDIR /app

#Copy the jar from the builder stage

COPY --from=builder /app/target/*.jar app.jar

#Expose the port the app runs on

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]