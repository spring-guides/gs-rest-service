# GS-REST-Service: Software Dependability Setup

## Overview

This is a Spring Boot REST service configured with comprehensive software dependability infrastructure, including automated testing, code quality analysis, and CI/CD pipelines.

## Architecture

### Core Application
- **Greeting Service**: Simple REST API that returns personalized greetings
- **Endpoint**: `GET /greeting?name=<optional>`
- **Response**: `{"id": <counter>, "content": "Hello, <name>!"}`

### Test Coverage
- **9 comprehensive unit tests** covering:
  - Default parameter behavior
  - Named parameters
  - Edge cases (empty strings, special characters, long inputs)
  - Response validation
  - JSON structure validation

## Build & Test

### Local Build
```bash
cd complete
mvn clean test
```

### Run Application
```bash
# Using Maven
mvn spring-boot:run

# Using Docker
docker-compose up rest-service

# Manual Docker
docker build -t gs-rest-service .
docker run -p 8080:8080 gs-rest-service
```

### Run Mutation Testing
```bash
cd complete
mvn org.pitest:pitest-maven:mutationCoverage
```

View results: `target/pit-reports/index.html`

## CI/CD Pipeline

### GitHub Actions Workflow
Located in: `.github/workflows/ci-cd.yml`

**Jobs:**
1. **Build & Test** (runs on Ubuntu with Java 17 & 21)
   - Compiles the project
   - Runs all unit tests
   - Executes PiTest mutation testing
   - Uploads reports as artifacts

2. **Code Quality**
   - SonarQube integration (requires configuration)
   - Static code analysis

3. **Security Scanning**
   - Trivy vulnerability scanner
   - GitGuardian secrets detection (requires API key)

4. **Docker Build**
   - Builds Docker image on main branch push

### Triggers
- ✅ On push to `main` or `develop` branches
- ✅ On pull requests to `main` or `develop` branches

## Docker Deployment

### Build Image
```bash
cd complete
docker build -t gs-rest-service:latest .
```

### Run Container
```bash
docker run -p 8080:8080 gs-rest-service:latest
```

### Docker Compose
```bash
docker-compose up
```

## Configuration

### Environment Variables
- `JAVA_OPTS`: JVM options (default: `-Xms256m -Xmx512m`)

### Application Properties
Located in: `src/main/resources/application.properties`

## Tools & Plugins

### Maven Plugins
| Plugin | Version | Purpose |
|--------|---------|---------|
| PiTest | 1.15.8 | Mutation testing |
| SonarQube Scanner | 3.10.0.2594 | Code quality analysis |
| Surefire | 3.5.4 | Unit test execution |
| Spring Boot Maven | Latest | Application packaging |

### Technology Stack
- **Framework**: Spring Boot 4.0.1
- **Language**: Java 17 (compatible with Java 21, 25)
- **Build Tool**: Maven 3.8+
- **Testing**: JUnit 5, Spring Test
- **Container**: Docker

## Security

### Current Measures
- ✅ Tests validate input handling
- ✅ Dependency scanning (Trivy)
- ✅ Secrets detection (GitGuardian)
- ⏳ SAST with SonarQube (ready to configure)

### To Enable Full Security Scanning

1. **GitGuardian Integration**
   ```bash
   # Add GitHub Secret
   GITGUARDIAN_API_KEY=<your-api-key>
   ```

2. **SonarQube Integration**
   - Host: http://localhost:9000 (or your SonarQube instance)
   - Configure `sonar.projectKey` in workflow

## Mutation Testing

### PiTest Results Interpretation

- **Killed Mutations**: ✅ Test successfully caught the bug
- **Survived Mutations**: ⚠️ Test missed a potential bug (weak test)
- **Mutation Score**: Target 80%+ (Higher is better)

### Run Local Analysis
```bash
cd complete
mvn clean org.pitest:pitest-maven:mutationCoverage
open target/pit-reports/index.html
```

## Health Checks

### Application Health
```bash
curl http://localhost:8080/greeting
```

### Docker Container Health
```bash
docker ps
# Check HEALTH column
```

## Troubleshooting

### Tests Fail with Java 25
- **Issue**: JaCoCo incompatibility with Java 25 (class file version 69)
- **Solution**: Tests run without JaCoCo; mutation testing still works with PiTest

### Docker Build Fails
- Ensure Maven wrapper is executable: `chmod +x complete/mvnw`
- Check Docker daemon is running

### SonarQube Connection Fails
- Verify SonarQube is running: `docker-compose up sonarqube`
- Wait 60 seconds for initialization
- Access at http://localhost:9000

## Next Steps

- [ ] Add JML formal specifications for core methods
- [ ] Integrate SonarQube with GitHub Actions
- [ ] Push Docker image to DockerHub
- [ ] Set up performance benchmarking with JMH
- [ ] Add API documentation (Swagger/OpenAPI)
- [ ] Implement database persistence

## Project Structure

```
gs-rest-service/
├── .github/workflows/
│   └── ci-cd.yml                    # GitHub Actions pipeline
├── complete/
│   ├── Dockerfile                   # Docker image definition
│   ├── docker-compose.yml           # Docker Compose config
│   ├── pom.xml                      # Maven configuration
│   ├── src/
│   │   ├── main/java/.../
│   │   │   ├── Greeting.java        # Response model (record)
│   │   │   ├── GreetingController.java  # REST controller
│   │   │   └── RestServiceApplication.java
│   │   └── test/java/.../
│   │       └── GreetingControllerTests.java (9 tests)
│   └── target/
│       ├── pit-reports/             # Mutation testing reports
│       └── surefire-reports/        # Unit test reports
├── README.adoc                      # Original documentation
└── README.md                        # This file
```

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [PiTest Mutation Testing](http://pitest.org/)
- [GitHub Actions](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)

## License

See LICENSE.txt

## Author

ZAKARIA (zkouari-f)
