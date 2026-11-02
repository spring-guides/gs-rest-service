# Requirements Phase

Execute the Requirements phase of the Agentic SDLC.

## Inputs

Use the configured Jira/Confluence MCP to retrieve the User Story.

Also inspect:

- existing repository structure
- existing application behavior
- relevant configuration
- existing tests

Use:

`.github/skills/requirements.skill.md`

## Tasks

1. Retrieve and understand the User Story.
2. Identify missing or ambiguous information.
3. Ask the human clarification questions when required.
4. Do not assume missing business requirements.
5. Define functional requirements.
6. Define non-functional requirements.
7. Define security requirements.
8. Define acceptance criteria.
9. Define constraints and dependencies.

## Output

Create:

`docs/requirements.md`

The document must contain:

- User Story
- Business Objective
- Scope
- Functional Requirements
- Non-Functional Requirements
- Security Requirements
- Acceptance Criteria
- Dependencies
- Constraints
- Assumptions
- Open Questions
- Approval Status

Set:

`Approval Status: PENDING`

## Restrictions

Do not modify application source code.

Wait for explicit human approval before Architecture begins.