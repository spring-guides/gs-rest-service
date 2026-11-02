# Code Review Phase

Perform a structured peer review of the implemented solution.

## Required Inputs

Read:

Required Inputs:
- Approved docs/requirements.md
- Approved docs/architecture.md
- Approved docs/design-review.md
- Approved docs/implementation-plan.md
- Implemented source code
- Implementation tests
- .github/skills/code-review.skill.md


## Review Areas

Evaluate:

### Correctness
Does the implementation satisfy the requirements?

### Security
Are secrets excluded?
Is input validated?
Are security boundaries respected?

### Error Handling
Are failures handled gracefully?

### Test Coverage
Are happy paths and edge cases covered?

### Code Clarity
Is the implementation readable?

### DRY
Is duplicated logic present?

### Maintainability
Does the implementation follow existing patterns?

### Dependency Safety
Are dependencies appropriate and safe?

### Performance
Are there obvious performance problems?

## Output

Create:

`docs/review-report.md`

Include:

- Review Scope
- Findings
- Severity
- Evidence
- Recommended Changes
- Positive Observations
- Final Recommendation
- Approval Status

Set:

`Approval Status: PENDING`

Do not modify source code during review.
### Review Scope

Evaluate security, error handling, performance, maintainability, and
correctness at the implementation/code level.

Do not re-review architectural decisions already approved during Design Review.
Focus on how the approved design was implemented.