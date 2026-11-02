---
name: code-review
description: Perform structured peer review of implementation for correctness, security, quality, and maintainability.
---

# Code Review Skill

## Review Checklist

### Correctness
Verify behavior against requirements.

### Security
Check:

- secrets
- input validation
- authentication/authorization where applicable
- unsafe configuration

### Error Handling
Check:

- API failures
- missing files
- empty results
- invalid input
- unexpected conditions

### Tests
Check:

- happy path
- negative path
- edge cases
- regression coverage

### Code Quality
Check:

- naming
- duplication
- complexity
- maintainability
- existing project conventions

### Dependencies
Check dependency versions and obvious security concerns.

## Output

Every finding must contain:

- severity
- file
- location
- issue
- impact
- recommendation

Do not modify code.