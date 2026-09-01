# Implementation Planning Phase

Create a dependency-ordered implementation plan.

## Required Inputs

Read:

- docs/requirements.md
- docs/architecture.md
- docs/design-review.md
- .github/skills/implementation-plan.skill.md

All previous artifacts must be approved.

## Tasks

Create a plan containing:

1. Work breakdown
2. Dependencies
3. Implementation order
4. Files/components affected
5. Test strategy
6. Security considerations
7. Rollback considerations
8. Blocked tasks
9. Acceptance criteria mapping

Each task must have:

- ID
- Description
- Dependency
- Expected files
- Test requirement
- Acceptance criteria

## Output

Create:

`docs/implementation-plan.md`

The document must contain:

- Dependency-ordered implementation tasks
- Task ID
- Description
- Dependencies
- Expected files
- Test requirements
- Acceptance criteria
- Approval Status: PENDING

Do not mark the document as APPROVED.

After creating docs/implementation-plan.md, stop and wait for explicit human approval.

The human must reply:

"implementation plan approved"

before the Implementation phase can begin.

## Restrictions

Do not modify application source code.

Wait for explicit human approval before Implementation.
