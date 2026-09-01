# Post-Implementation Hook

## Purpose

Validate the state of the repository after implementation changes
before the workflow proceeds to Code Review and Verification.

## Trigger

Execute after implementation work has been completed.

## Required Checks

The implementation phase must confirm:

1. Source code changes are limited to approved scope.
2. Existing functionality has not been intentionally removed.
3. New functionality has corresponding tests.
4. No secrets or credentials were added.
5. The project structure remains intact.
6. The implementation follows the approved implementation plan.
7. The application can be built successfully.

## Required Evidence

The implementation phase should produce:

- source-code changes
- unit/integration tests where applicable
- implementation summary
- list of modified files
- build/test result

## Important Boundary

The Implementation phase may make source-code changes.

The Implementation phase should NOT own the final verification decision.

Final verification belongs to the Verification phase.

## Next Phase

After implementation:

Implementation
    ↓
Code Review
    ↓
Human Approval
    ↓
Verification
    ↓
Human Approval
    ↓
Pull Request