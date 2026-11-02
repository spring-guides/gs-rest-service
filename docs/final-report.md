# GHCP Capstone Final Report

## Project Overview

This capstone demonstrates an AI-assisted Software Development Lifecycle (SDLC) using GitHub Copilot for a Spring Boot REST service. The project follows a structured workflow from requirements analysis through pull request preparation while keeping human review and approval at every stage.

## Project Objective

Implement a personalized greeting endpoint in the Spring Boot application and demonstrate the complete Agentic SDLC workflow using GitHub Copilot.

---

# SDLC Workflow

## Phase 1 – Requirements

### Objective
Generate business requirements for the existing Spring Boot application.

### Deliverable
- docs/requirements.md

### Status
Completed

---

## Phase 2 – Architecture

### Objective
Design the solution architecture based on approved requirements.

### Deliverable
- docs/architecture.md

### Status
Completed

---

## Phase 3 – Design Review

### Objective
Review the architecture for assumptions, risks, dependencies, and improvements.

### Deliverable
- docs/design-review.md

### Status
Completed

---

## Phase 4 – Implementation Plan

### Objective
Create a dependency-based implementation plan.

### Deliverable
- docs/implementation-plan.md

### Status
Completed

---

## Phase 5 – Implementation

### Objective
Implement the approved greeting feature.

### Implemented Components

- Greeting REST Controller
- Greeting Service
- Greeting Model
- Unit Tests

### Result

Implemented a REST endpoint:

```
GET /greeting
```

Supports:

```
/greeting
```

and

```
/greeting?name=Gowri
```

Status

Completed

---

## Phase 6 – Code Review

### Objective

Review implementation for:

- Code quality
- Best practices
- Security
- Maintainability

Deliverable

- docs/review-report.md

Status

Completed

---

## Phase 7 – Verification

### Verification Activities

Executed:

```
mvn clean
```

```
mvn test
```

```
mvn spring-boot:run
```

Verified:

- Application starts successfully
- Greeting endpoint responds correctly
- Unit tests pass

Deliverable

- docs/verification.md

Status

Completed

---

## Phase 8 – Pull Request

Generated:

- docs/pr-description.md

Created Pull Request:

Feature/agentic sdlc

Status

Completed

---

# Build Results

| Activity | Result |
|----------|--------|
| Maven Clean | Passed |
| Maven Build | Passed |
| Unit Tests | Passed |
| Spring Boot Startup | Passed |
| REST Endpoint | Passed |

---

# Documentation Generated

- requirements.md
- architecture.md
- design-review.md
- implementation-plan.md
- review-report.md
- verification.md
- pr-description.md
- final-report.md

---

# AI Capabilities Demonstrated

- Requirements generation
- Architecture generation
- Design review
- Implementation planning
- Guided code implementation
- Code review
- Verification planning
- Pull Request generation

---

# Lessons Learned

- AI can accelerate SDLC documentation.
- Human approval remains essential before implementation.
- GitHub Copilot can assist throughout the complete development lifecycle.
- GitHub MCP integration can extend the workflow to Jira, GitHub, and Confluence.

---

# Conclusion

The project successfully demonstrates an end-to-end AI-assisted Software Development Lifecycle using GitHub Copilot.

All planned SDLC phases were completed successfully, including documentation generation, implementation, testing, verification, and pull request preparation.

The application builds successfully, all unit tests pass, and the greeting endpoint functions as expected.

The capstone objectives have been achieved successfully.