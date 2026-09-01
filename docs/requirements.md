# Requirements Document

**Jira Story:** [SCRUM-3 — Display personalized greeting message](https://gowri1020k-1784826767697.atlassian.net/browse/SCRUM-3)
**Project:** Capstone For Codemie
**Date:** 2026-08-11
**Approval Status:** APPROVED

---

## User Story

> As a user,
> I want to receive a greeting message using my name,
> So that I receive a personalized response.

---

## Business Objective

Introduce a REST endpoint to the existing Spring Boot application that returns a personalized greeting. This feature serves as the reference implementation for demonstrating an AI-assisted Agentic SDLC workflow using GitHub Copilot Agent Mode. It validates end-to-end documentation generation across all SDLC phases (Requirements, Architecture, Design Review, Implementation Planning, Implementation, Code Review, Verification, and Pull Request generation).

---

## Scope

**In Scope:**
- A single REST GET endpoint that returns a personalized greeting message
- An optional `name` query parameter with a default value of `"World"`
- JSON response body
- HTTP 200 status for successful requests
- Unit tests covering the greeting behavior and default handling
- Implementation within the `initial/` Spring Boot application using the existing package structure

**Out of Scope:**
- Authentication / authorization
- Database or persistence
- UI / frontend
- External service integrations
- Any modifications to unrelated application behavior

---

## Functional Requirements

| ID    | Requirement |
|-------|-------------|
| FR-01 | The application shall expose a REST GET endpoint at `/greeting`. |
| FR-02 | The endpoint shall accept an optional query parameter named `name`. |
| FR-03 | When `name` is provided, the response shall return the greeting `"Hello, {name}!"`. |
| FR-04 | When `name` is not provided, the endpoint shall default the name to `"World"`, returning `"Hello, World!"`. |
| FR-05 | The endpoint shall return HTTP status 200 for all valid requests. |
| FR-06 | The response shall be serialized as JSON containing only the greeting message string. No `id` or additional fields are required. |
| FR-07 | The existing Spring Boot application startup behavior shall be preserved. |
| FR-08 | Unit tests shall verify the greeting behavior for both the named and default cases. |

---

## Non-Functional Requirements

| ID     | Requirement |
|--------|-------------|
| NFR-01 | The implementation shall use Java 17. |
| NFR-02 | The build tool shall be Maven. |
| NFR-03 | The framework shall be Spring Boot (existing version used in `initial/pom.xml`). |
| NFR-04 | The implementation shall follow the existing package structure (`com.example.restservice`). |
| NFR-05 | No additional third-party dependencies shall be introduced unless strictly necessary. |
| NFR-06 | The Maven build (`mvn clean verify`) shall succeed without errors or test failures. |

---

## Security Requirements

| ID    | Requirement |
|-------|-------------|
| SR-01 | The `name` query parameter shall be treated as untrusted input; it must not be evaluated, executed, or stored — only reflected in the greeting string. |
| SR-02 | The endpoint shall not expose internal application state, stack traces, or sensitive data in any response. |
| SR-03 | No authentication or authorization mechanism is required for this endpoint (public, read-only, stateless). |
| SR-04 | The implementation shall not introduce any OWASP Top 10 vulnerabilities. The `name` value shall be used only in string formatting and never in SQL, shell commands, or template engines that could enable injection attacks. |

---

## Acceptance Criteria

| ID    | Criterion |
|-------|-----------|
| AC-01 | `GET /greeting` returns HTTP 200. |
| AC-02 | `GET /greeting` (no `name` param) returns a JSON response containing `"World"` in the greeting. |
| AC-03 | `GET /greeting?name=Alice` returns a JSON response containing `"Alice"` in the greeting. |
| AC-04 | The response body is valid JSON. |
| AC-05 | All unit tests pass. |
| AC-06 | `mvn clean verify` completes successfully. |

---

## Dependencies

| ID     | Dependency | Notes |
|--------|------------|-------|
| DEP-01 | Spring Boot (existing version in `initial/pom.xml`) | No version change required |
| DEP-02 | Java 17 JDK | Must be available in the build environment |
| DEP-03 | Maven wrapper (`mvnw`) | Already present in `initial/` |

---

## Constraints

| ID     | Constraint |
|--------|------------|
| CON-01 | Implementation must reside in the `initial/` subproject, not `complete/`. |
| CON-02 | The existing package structure `com.example.restservice` must be retained. |
| CON-03 | No database, external API, or UI work is permitted in this story. |
| CON-04 | The implementation must not break existing application startup. |

---

## Assumptions

| ID     | Assumption |
|--------|------------|
| ASM-01 | The `initial/` Spring Boot application compiles and starts successfully before this story begins. |
| ASM-02 | The `complete/` folder contains a reference implementation that may be consulted but must not be copied verbatim into `initial/`. |
| ASM-03 | The Jira story SCRUM-3 description is the authoritative source of business requirements; no additional stakeholder input is required. |
| ASM-04 | The `name` parameter value is a plain string with no length or character-set constraints beyond what Spring MVC applies by default. |

---

## Open Questions

| ID    | Question | Status |
|-------|----------|--------|
| OQ-01 | Is there a maximum length restriction for the `name` parameter? | **Resolved:** No specific limit required. |
| OQ-02 | Should the greeting message format be exactly `"Hello, {name}!"` or is the wording flexible? | **Resolved:** Format is exactly `"Hello, {name}!"`. |
| OQ-03 | Should the JSON response include a unique request identifier (e.g., an auto-incremented `id` field as seen in the reference implementation)? | **Resolved:** Response contains only the greeting message; no `id` field. |

---

## Approval Status

**APPROVED** — Requirements approved by the human on 2026-08-11. Ready to proceed to the Architecture phase.
