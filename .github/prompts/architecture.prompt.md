# Architecture Phase

Execute the Architecture phase.

## Required Inputs

Read:

- docs/requirements.md
- .github/skills/architecture.skill.md

`docs/requirements.md` must have:

`Approval Status: APPROVED`

If it is not approved, stop and report the missing approval.

## Tasks

1. Analyze the approved requirements.
2. Inspect the existing application architecture.
3. Propose the target architecture.
4. Identify components and responsibilities.
5. Define data flow.
6. Identify technology choices.
7. Identify integration points.
8. Identify security boundaries.
9. Identify risks and constraints.
10. Ensure the proposal preserves existing functionality.

## Output

Create:

`docs/architecture.md`

Include:

- Architecture Overview
- Existing Architecture
- Proposed Architecture
- Components
- Responsibilities
- Data Flow
- Technology Decisions
- Integration Points
- Security Considerations
- Risks
- Constraints
- Alternatives Considered
- Requirements Traceability
- Approval Status

Set:

`Approval Status: PENDING`

## Restrictions

Do not modify application source code.

Wait for human approval before Design Review.