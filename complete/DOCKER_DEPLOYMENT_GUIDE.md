# Docker Deployment Guide - GS-REST-Service

## Overview

This guide explains how to build, test, and push the GS-REST-Service Docker image to DockerHub.

---

## Prerequisites

### 1. Docker Desktop Installation

**Windows:**
- Download: https://www.docker.com/products/docker-desktop
- Install and start Docker Desktop
- Verify: `docker --version`

**Mac:**
```bash
brew install --cask docker
# Start Docker Desktop from Applications
```

**Linux:**
```bash
sudo apt-get install docker.io docker-compose
sudo usermod -aG docker $USER
```

### 2. DockerHub Account

- Create account: https://hub.docker.com/
- Verify email
- Note your username (e.g., `zkouari-f`)

### 3. Docker CLI Authentication

```bash
docker login
# Enter DockerHub username
# Enter DockerHub password or access token
```

Verify:
```bash
docker info | grep Username
```

---

## Step 1: Build Docker Image Locally

### Build Command

```bash
cd complete
docker build -t gs-rest-service:latest .
```

### With Version Tag

```bash
docker build -t gs-rest-service:1.0.0 -t gs-rest-service:latest .
```

### Build with Specific Java Version

```bash
docker build \
  --build-arg JAVA_VERSION=17 \
  -t gs-rest-service:java17 .
```

### Expected Output

```
[1/2] FROM eclipse-temurin:17-jdk-jammy
[2/2] RUN ./mvnw clean package -DskipTests
...
Successfully built abc123def456
Successfully tagged gs-rest-service:latest
```

**Build time:** ~3-5 minutes (first time), ~30 seconds (cached)

---

## Step 2: Test Docker Image Locally

### Run Container

```bash
docker run -p 8080:8080 gs-rest-service:latest
```

### Expected Output

```
.   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v4.0.1)

2026-01-23 23:30:00 INFO: Greeting Service started on port 8080
```

### Test the Application

**In another terminal:**

```bash
curl http://localhost:8080/greeting
# Response: {"id":1,"content":"Hello, World!"}

curl "http://localhost:8080/greeting?name=Docker"
# Response: {"id":2,"content":"Hello, Docker!"}
```

### Stop Container

```bash
# Press Ctrl+C in the running terminal
# OR
docker stop <container_id>
```

---

## Step 3: Tag for DockerHub

### Tag Format

```bash
docker tag gs-rest-service:latest zkouari-f/gs-rest-service:latest
docker tag gs-rest-service:1.0.0 zkouari-f/gs-rest-service:1.0.0
```

**Replace `zkouari-f` with your DockerHub username**

### Verify Tags

```bash
docker images | grep gs-rest-service
```

Expected:
```
zkouari-f/gs-rest-service      latest              abc123def456   3 minutes ago   250MB
zkouari-f/gs-rest-service      1.0.0               abc123def456   3 minutes ago   250MB
gs-rest-service                latest              abc123def456   3 minutes ago   250MB
```

---

## Step 4: Push to DockerHub

### Login to DockerHub

```bash
docker login
# Username: zkouari-f
# Password: <your-password-or-token>
```

### Push Command

```bash
# Push latest tag
docker push zkouari-f/gs-rest-service:latest

# Push version tag
docker push zkouari-f/gs-rest-service:1.0.0

# Push all tags
docker push zkouari-f/gs-rest-service
```

### Expected Output

```
The push refers to repository [docker.io/zkouari-f/gs-rest-service]
abc123def456: Pushed
def456ghi789: Pushed
ghi789jkl012: Pushed
latest: digest: sha256:abc123...def456 size: 2048
1.0.0: digest: sha256:abc123...def456 size: 2048
```

### Verify on DockerHub

1. Go to: https://hub.docker.com/r/zkouari-f/gs-rest-service
2. Should see:
   - Public repository
   - Tags: `latest`, `1.0.0`
   - Pull command: `docker pull zkouari-f/gs-rest-service:1.0.0`

---

## Step 5: Pull and Run from DockerHub

### Pull from DockerHub

```bash
# Latest version
docker pull zkouari-f/gs-rest-service:latest

# Specific version
docker pull zkouari-f/gs-rest-service:1.0.0
```

### Run from DockerHub

```bash
docker run -p 8080:8080 zkouari-f/gs-rest-service:latest
```

### Verify It Works

```bash
curl http://localhost:8080/greeting?name=Production
```

---

## Docker Compose Deployment

### Local Testing with Compose

```bash
cd complete
docker-compose up
```

### Services Started

1. **REST Service** (port 8080)
   - Health check enabled
   - Auto-restart on failure

2. **SonarQube** (port 9000)
   - Code quality analysis
   - Optional for local development

### Access Services

- **REST API:** http://localhost:8080/greeting
- **SonarQube:** http://localhost:9000

### Stop Services

```bash
docker-compose down
```

### Clean Up

```bash
docker-compose down -v  # Also remove volumes
```

---

## Multi-Stage Build Explanation

### Stage 1: Builder

```dockerfile
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /build
COPY pom.xml .
COPY mvnw .
RUN ./mvnw clean package -DskipTests
```

**Purpose:**
- Contains full JDK + Maven
- Compiles application
- Large intermediate image (~800MB)

### Stage 2: Runtime

```dockerfile
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /build/target/rest-service-complete-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Purpose:**
- Uses lightweight JRE only (not JDK)
- Copies only the JAR
- Final image size: ~250MB (vs ~1GB for single-stage)

### Benefits

| Metric | Single-Stage | Multi-Stage |
|--------|--------------|-------------|
| Build time | 3-5 min | 3-5 min |
| Final size | ~1GB | ~250MB |
| Security | Contains build tools | Build tools removed |
| Push time | 5+ min | 1-2 min |

---

## Image Details

### Size Breakdown

```
zkouari-f/gs-rest-service:latest
├── Base JRE image (100MB)
├── Spring Boot runtime (50MB)
├── Application JAR (100MB)
└── Total: ~250MB
```

### Layers

```bash
docker history zkouari-f/gs-rest-service:latest
```

### Security Scanning

```bash
# Scan with Trivy
trivy image zkouari-f/gs-rest-service:latest

# Scan with Docker Scout
docker scout cves zkouari-f/gs-rest-service:latest
```

---

## Production Deployment

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gs-rest-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: gs-rest-service
  template:
    metadata:
      labels:
        app: gs-rest-service
    spec:
      containers:
      - name: rest-service
        image: zkouari-f/gs-rest-service:1.0.0
        ports:
        - containerPort: 8080
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /greeting
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

### Docker Swarm Deployment

```bash
docker service create \
  --name gs-rest-service \
  --publish 8080:8080 \
  --replicas 3 \
  zkouari-f/gs-rest-service:latest
```

### AWS ECS Deployment

```json
{
  "name": "gs-rest-service",
  "image": "zkouari-f/gs-rest-service:1.0.0",
  "memory": 512,
  "cpu": 256,
  "portMappings": [{
    "containerPort": 8080,
    "hostPort": 8080,
    "protocol": "tcp"
  }],
  "essential": true
}
```

---

## Troubleshooting

### Issue: Docker daemon not running

```bash
# Windows/Mac
# Open Docker Desktop application

# Linux
sudo systemctl start docker
```

### Issue: Permission denied

```bash
# Add user to docker group (Linux)
sudo usermod -aG docker $USER
newgrp docker
```

### Issue: Build fails - Maven download

**Solution:** Increase Docker memory
- Docker Desktop → Preferences → Resources
- Increase memory to 4GB+

### Issue: Push fails - authentication

```bash
# Re-login
docker logout
docker login

# Or use access token instead of password
```

### Issue: Image too large

**Solution:** Check layers
```bash
docker history zkouari-f/gs-rest-service:latest

# Optimize Dockerfile:
# - Use .dockerignore
# - Remove build artifacts
# - Use slim base images
```

---

## CI/CD Integration

### GitHub Actions

Already configured in `.github/workflows/ci-cd.yml`:

```yaml
- name: Set up Docker Buildx
  uses: docker/setup-buildx-action@v3

- name: Build Docker image (local test)
  run: docker build -t gs-rest-service .

# Future: Add push to DockerHub
- name: Login to DockerHub
  uses: docker/login-action@v2
  with:
    username: ${{ secrets.DOCKERHUB_USERNAME }}
    password: ${{ secrets.DOCKERHUB_TOKEN }}

- name: Push to DockerHub
  uses: docker/build-push-action@v4
  with:
    push: true
    tags: zkouari-f/gs-rest-service:latest
```

### Setup GitHub Secrets

1. Go to: https://github.com/zkouari-f/gs-rest-service/settings/secrets
2. Add secrets:
   - `DOCKERHUB_USERNAME`: zkouari-f
   - `DOCKERHUB_TOKEN`: (from DockerHub account settings)

---

## Performance Metrics

### Build Performance

| Scenario | Time | Cache |
|----------|------|-------|
| First build | 3-5 min | Cold |
| Rebuild (no changes) | 5 sec | Hot |
| Update code only | 30 sec | Hot (reuse layers) |
| Update Maven deps | 1-2 min | Warm |

### Runtime Performance

| Metric | Value |
|--------|-------|
| Startup time | ~8 seconds |
| Memory usage | 256-512 MB |
| CPU usage (idle) | <5% |
| Response time | <50ms |

---

## Checklist: Push to DockerHub

- [ ] Docker Desktop installed and running
- [ ] Docker login successful (`docker login`)
- [ ] DockerHub account created
- [ ] Local build successful (`docker build ...`)
- [ ] Local test successful (`docker run ...`)
- [ ] Image tagged correctly (`zkouari-f/gs-rest-service:latest`)
- [ ] Image pushed (`docker push ...`)
- [ ] Verified on DockerHub website
- [ ] Pull from DockerHub works (`docker pull ...`)
- [ ] Documentation updated

---

## Summary of Commands

```bash
# Build
docker build -t gs-rest-service:1.0.0 -t gs-rest-service:latest .

# Test locally
docker run -p 8080:8080 gs-rest-service:latest

# Tag for DockerHub
docker tag gs-rest-service:latest zkouari-f/gs-rest-service:latest

# Login
docker login

# Push
docker push zkouari-f/gs-rest-service:latest

# Pull from DockerHub
docker pull zkouari-f/gs-rest-service:latest

# Run from DockerHub
docker run -p 8080:8080 zkouari-f/gs-rest-service:latest
```

---

## References

- **Docker Official:** https://docs.docker.com/
- **DockerHub:** https://hub.docker.com/
- **Spring Boot Docker:** https://spring.io/guides/gs/spring-boot-docker/
- **Kubernetes:** https://kubernetes.io/

---

**Guide Version:** 1.0  
**Last Updated:** 2026-01-23  
**Status:** Ready for Docker Desktop execution
