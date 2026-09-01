# Architecture Document

## Governance

- Status: Drafted for Architecture phase (SCRUM-3)
- Approval Status: APPROVED
- Approver: Human Reviewer
- Approval Date: 2026-08-11
- Decisions:
  - Keep a single Spring Boot application in `initial/` with no new modules.
  - Preserve package root `com.example.restservice` and add only minimal REST components.
  - Use controller + service separation so HTTP concerns and greeting logic remain decoupled.
  - Return JSON with only `message` field for `/greeting`, matching approved requirements.
  - Keep persistence and external integrations out of scope.
- Open Issues:
  - None blocking architecture approval.
  - Clarification for future stories: explicit behavior for blank/whitespace `name` values is not defined in SCRUM-3 requirements.

## Architecture Overview

SCRUM-3 uses a minimal layered Spring MVC architecture inside the existing `initial/` application. The architecture supports one public REST endpoint (`GET /greeting`) that accepts an optional `name` query parameter and returns a JSON payload containing only the greeting message. The solution is intentionally lightweight and preserves existing startup behavior.

## Existing Architecture

Current `initial/` project context:

- Single Spring Boot application (`RestServiceApplication`) using Java 17 and Maven.
- Web stack provided by Spring Boot Web MVC starter.
- No database, no external service clients, and no UI.
- Existing code structure already includes:
  - Bootstrap class in `com.example.restservice`
  - `GreetingController` in `com.example.restservice.controller`
  - `GreetingService` in `com.example.restservice.service`
  - `GreetingResponse` DTO in `com.example.restservice.controller`
  - Unit/context tests in `src/test/java`

This is consistent with a minimal monolithic REST service and should be retained.

## Proposed Architecture

The target architecture for SCRUM-3 is a thin-controller pattern with isolated greeting logic:

1. Controller handles request mapping and parameter binding.
2. Service constructs greeting output and default behavior.
3. DTO serializes response to JSON.
4. Tests validate startup and greeting behavior for named and default flows.

No additional layers (repository, integration clients, async messaging) are introduced because they are unnecessary for this scope.

## Components

1. Application Bootstrap
- Class: `RestServiceApplication`
- Responsibility: application startup and Spring context initialization.

2. Greeting API Controller
- Class: `GreetingController`
- Responsibility: expose `GET /greeting`, parse optional `name`, return response object.

3. Greeting Domain Service
- Class: `GreetingService`
- Responsibility: apply greeting rule and defaulting logic.

4. Response DTO
- Class: `GreetingResponse`
- Responsibility: response contract with single field `message`.

5. Test Layer
- Classes: `GreetingControllerTest`, `RestServiceApplicationTests`
- Responsibility: verify business behavior and application context load.

## Responsibilities

- Controller: transport concerns (HTTP mapping, query parameter binding, response object creation).
- Service: business rule concerns (default when `name` is not provided + output format `Hello, {name}!`).
- DTO: API schema concerns (JSON shape).
- Spring Boot runtime: dependency injection, request dispatching, JSON serialization.

## Data Flow

1. Client sends `GET /greeting` with optional `name`.
2. Spring MVC routes request to `GreetingController.greeting(...)`.
3. Controller forwards `name` to `GreetingService.greet(...)`.
4. Service computes effective name (`World` when `name` is not provided) and returns greeting string.
5. Controller wraps value in `GreetingResponse(message)`.
6. Spring serializes DTO to JSON and returns HTTP 200.

## Technology Decisions

- Java 17: satisfies NFR-01 and existing build configuration.
- Maven build: satisfies NFR-02 and project convention.
- Spring Boot (existing version in `initial/pom.xml`, currently 4.0.7) + Web MVC starter: satisfies NFR-03 and DEP-01.
- JUnit 5 + Spring Boot test starter: supports FR-08 and context validation.
- No additional dependencies: satisfies NFR-05.

## Integration Points

- Inbound: HTTP client requests to `/greeting`.
- Internal: Spring dependency injection between controller and service.
- Outbound: none (no DB, queue, cache, or external API).

## Security Considerations

- `name` is treated as untrusted input and used only for string composition (SR-01, SR-04).
- Endpoint is read-only and stateless; no credential handling is introduced (SR-03).
- Response contract exposes only message content; no stack traces/internal state intended (SR-02).
- No persistence or command execution paths, reducing injection risk surface.

## Risks

- Requirement interpretation risk around blank input behavior (`name=`) if future policy changes.
- Contract drift risk if additional response fields are added in later refactors.
- Regression risk if startup wiring changes without corresponding tests.

## Constraints

- Implement only within `initial/` (CON-01).
- Preserve package base `com.example.restservice` (CON-02).
- No DB/external API/UI additions (CON-03).
- Preserve startup behavior (CON-04, FR-07).

## Alternatives Considered

1. Controller-only implementation (no service class)
- Rejected: increases coupling of HTTP and business logic; weakens test isolation.

2. Include numeric id in response (reference-style payload)
- Rejected: conflicts with FR-06 requiring only greeting message.

3. Add repository/persistence abstraction
- Rejected: unnecessary complexity for stateless greeting story.

## Requirements Traceability

| Requirement | Architectural Coverage |
|---|---|
| FR-01 | `GreetingController` exposes `GET /greeting`. |
| FR-02 | Optional `name` parameter bound in controller method signature. |
| FR-03 | `GreetingService` formats `Hello, {name}!` for provided name. |
| FR-04 | `GreetingService` defaults to `World` when `name` is not provided. |
| FR-05 | Standard successful controller return path yields HTTP 200. |
| FR-06 | `GreetingResponse` contains only `message` for JSON serialization. |
| FR-07 | `RestServiceApplication` startup path preserved; no architectural disruption. |
| FR-08 | `GreetingControllerTest` and context-load test provide unit/startup verification. |
| NFR-01..03 | Java 17, Maven, Spring Boot retained from existing setup. |
| NFR-04 | Package structure under `com.example.restservice` maintained. |
| NFR-05 | No new third-party dependencies required. |
| NFR-06 | Architecture supports `mvn clean verify` with existing test approach. |
| SR-01..04 | Input treated as data only; no sensitive data exposure or execution pathways. |

## Approval Status

Approval Status: APPROVED
