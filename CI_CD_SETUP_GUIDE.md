# CI/CD Pipeline with Test Suite & Code Coverage

## ✅ Configuration Complete

Your Spring Boot project now has a comprehensive CI/CD pipeline with automated testing and code coverage reporting.

---

## 📋 What Was Configured

### 1. **JaCoCo Code Coverage (pom.xml)**
   - ✅ Enabled in Maven build
   - Generates coverage reports after tests run
   - Enforces minimum 50% line coverage threshold
   - Generates both HTML and XML reports

### 2. **GitHub Actions Workflow (.github/workflows/ci-cd.yml)**

#### Build Job (Runs on: Push & Pull Requests)
   - **Java Versions:** Tests on Java 17 & 21
   - **Build Step:** `mvn clean package -DskipTests`
   - **Test Step:** `mvn test` (Surefire runs unit tests)
   - **Coverage Step:** `mvn jacoco:report` (Generates JaCoCo HTML/XML)
   - **Coverage Upload:** Integrated with Codecov for badge & tracking
   - **Artifacts:**
     - `jacoco-coverage-report-java-*` (HTML reports)
     - `surefire-report-java-*` (Test results)
     - `pitest-report-java-*` (Mutation testing)

#### Code Quality Job
   - Runs SonarQube analysis (optional - requires secrets)
   - Performs static code analysis

#### Security Scan Job
   - Runs Trivy vulnerability scanner
   - GitGuardian secret detection (optional)

#### Docker Build Job
   - Builds & pushes Docker image on main branch push
   - Requires DOCKERHUB_USERNAME & DOCKERHUB_TOKEN secrets

---

## 🚀 How to Use

### 1. **Run Tests Locally**
```bash
cd complete
mvn test
```

### 2. **Generate Coverage Report Locally**
```bash
mvn jacoco:report
# Open: target/site/jacoco/index.html
```

### 3. **Push to GitHub**
```bash
git add .
git commit -m "Add CI/CD with test suite and code coverage"
git push origin main
```

### 4. **View Results**
   - **GitHub Actions:** Go to your repo → Actions tab
   - **Artifacts:** Download coverage/test reports from workflow run
   - **Codecov:** (Optional) Coverage badge & tracking at codecov.io

---

## 🔐 GitHub Secrets (Optional Enhancements)

To enable optional features, add these secrets to your repo (Settings → Secrets):

| Secret | Purpose | Example |
|--------|---------|---------|
| `DOCKERHUB_USERNAME` | Docker Hub push | `zakaria2329` |
| `DOCKERHUB_TOKEN` | Docker Hub authentication | Access token from Docker Hub |
| `SONAR_HOST_URL` | SonarQube server | `https://sonarqube.example.com` |
| `SONAR_TOKEN` | SonarQube authentication | Token from SonarQube |
| `GITGUARDIAN_API_KEY` | Secret scanning | API key from GitGuardian |
| `CODECOV_TOKEN` | Codecov (if private repo) | Token from codecov.io |

---

## 📊 Understanding the Reports

### JaCoCo Coverage Report
- **Location:** `target/site/jacoco/index.html`
- **Metrics:** Line coverage, branch coverage, complexity
- **Format:** HTML (interactive) & XML (machine-readable)

### Test Results
- **Location:** `target/surefire-reports/`
- **Contains:** Individual test outputs and summaries

### Mutation Testing (PiTest)
- **Location:** `target/pit-reports/`
- **Purpose:** Tests the quality of your unit tests

---

## ✨ Key Features

| Feature | Status | Details |
|---------|--------|---------|
| Unit Testing | ✅ Enabled | Surefire + JUnit 5 |
| Code Coverage | ✅ Enabled | JaCoCo with 50% minimum |
| Mutation Testing | ✅ Enabled | PiTest for test quality |
| Static Analysis | ✅ Enabled | SonarQube ready |
| Security Scanning | ✅ Enabled | Trivy vulnerability scanner |
| Docker Build | ✅ Enabled | Auto-push to Docker Hub |
| Multi-Java Test | ✅ Enabled | Tests on Java 17 & 21 |
| Artifact Storage | ✅ Enabled | 30-day retention |

---

## 🎯 Coverage Enforcement

The JaCoCo plugin enforces a **minimum 50% line coverage** across all non-test packages:

```xml
<limit>
  <counter>LINE</counter>
  <value>COVEREDRATIO</value>
  <minimum>0.50</minimum>
</limit>
```

**To adjust:** Edit `pom.xml` in the jacoco-maven-plugin `<minimum>` value.

---

## 📝 Next Steps

1. **Add More Tests:** Increase coverage beyond 50%
2. **Configure Codecov:** For coverage badges and trend tracking
3. **Set Branch Protection:** Require PR checks to pass on GitHub
4. **Add SonarQube:** For advanced code quality metrics
5. **Enable Secret Scanning:** Add GITGUARDIAN_API_KEY for security

---

## 📞 Troubleshooting

### Tests Fail Locally but Pass in CI
- Ensure Java version matches (21 LTS)
- Run: `mvn clean test -U` to update dependencies

### Coverage Report Not Generated
- Verify Surefire tests ran successfully
- Check: `mvn jacoco:report` runs without errors

### Docker Push Fails
- Add DOCKERHUB_USERNAME & DOCKERHUB_TOKEN secrets
- Verify credentials are correct

---

## 🔗 Reference Links

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/)
- [GitHub Actions Java Setup](https://github.com/actions/setup-java)
- [Codecov Integration](https://codecov.io/)
- [SonarQube Integration](https://docs.sonarqube.org/)

---

**Last Updated:** January 26, 2026
**Java Version:** 21 LTS
**Build Tool:** Maven
**Framework:** Spring Boot 4.0.1
