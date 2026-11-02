# Pre-PR Hook

## Purpose

Prevent Pull Request preparation or creation until the implementation
has passed Code Review and Verification.

## Required Artifacts

Before PR preparation begins, the following must exist:

- `docs/requirements.md`
- `docs/architecture.md`
- `docs/design-review.md`
- `docs/implementation-plan.md`
- `docs/review-report.md`
- `docs/verification.md`

## Required Approval Gates

The following must be approved:

1. Requirements
2. Architecture
3. Design Review
4. Implementation Plan
5. Code Review
6. Verification

## PR Preconditions

The PR phase must verify:

- implementation is complete
- code review is complete
- verification is complete
- tests have passed
- known limitations are documented
- no unresolved blocking review findings remain

## PR Content Requirements

Copilot must generate:

### Summary

2–3 sentence description of what was built and why.

### Changes Made

Bulleted list of modified and added files.

### Test Evidence

Include test execution results or CI evidence.

### Known Limitations

Document:

- Not Found items
- unsupported scenarios
- known limitations
- out-of-scope functionality

### Reviewer Checklist

Include a checklist covering:

- requirements
- architecture
- correctness
- security
- error handling
- test coverage
- maintainability
- verification evidence

## Human Approval

Copilot may prepare the PR description.

Copilot must not merge the PR without explicit human approval.

## Prohibited

The PR hook must prevent:

- bypassing code review
- bypassing verification
- creating a PR with failed mandatory verification
- silently marking review or verification as approved