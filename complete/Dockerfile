# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Copy Maven wrapper and project files
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn/

# Copy source code
COPY src src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /build/target/rest-service-complete-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/greeting || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
