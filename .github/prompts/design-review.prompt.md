# Design Review Phase

Act as a senior software architect reviewing the proposed architecture.

## Required Inputs

Read:

- docs/requirements.md
- docs/architecture.md
- .github/skills/design-review.skill.md

Both artifacts must be:

`Approval Status: APPROVED`

If not, stop.

## Review Areas

Evaluate:

- Requirements coverage
- Architecture correctness
- Component responsibilities
- Data flow
- Scalability
- Maintainability
- Security
- Error handling
- Observability
- Testability
- Dependencies
- Failure scenarios
- Rollback strategy
- Operational concerns

## Output

Create:

`docs/design-review.md`

Include:

- Review Scope
- Requirements Traceability
- Findings
- Risks
- Gaps
- Decisions
- Recommended Changes
- Accepted Risks
- Final Recommendation
- Approval Status

Classify findings as:

- BLOCKER
- HIGH
- MEDIUM
- LOW
- INFORMATIONAL

Set:

`Approval Status: PENDING`

## Restrictions

Do not modify application source code.

Architecture changes require human approval.

### Review Scope

Evaluate security, error handling, scalability, and observability at the
architecture and design level.

Do not review implementation-specific code quality or implementation defects.
Those concerns belong to the Code Review phase.