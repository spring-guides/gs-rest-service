# GitHub Copilot Instructions

## Purpose

This repository demonstrates an Agentic Software Development Lifecycle (SDLC)
using GitHub Copilot, custom agents, prompts, skills, hooks, and MCP integrations.

Copilot must follow the SDLC workflow defined below and keep a human in the loop
at every phase boundary.

---

## SDLC Phase Order

Execute phases in this exact order:

1. Requirements
2. Architecture
3. Design Review
4. Implementation Planning
5. Implementation
6. Code Review
7. Verification
8. Pull Request
 
A later phase must not begin until the required artifact from the previous phase
has been reviewed and explicitly approved by the human.

---

## Phase Handoff Contract

| Phase | Required Input | Output |
|---|---|---|
| Requirements | Jira/Confluence/User Story | docs/requirements.md |
| Architecture | Approved requirements.md | docs/architecture.md |
| Design Review | Approved requirements.md + architecture.md | docs/design-review.md |
| Implementation Planning | Approved architecture.md + design-review.md | docs/implementation-plan.md |
| Implementation | Approved implementation-plan.md | Source code + tests |
| Code Review | Implemented source + verification.md | docs/review-report.md |
| Verification | Implemented source + tests + approved plan | docs/verification.md |
| Pull Request | All approved SDLC artifacts | docs/pr-description.md |
 
---

## Approval Governance

Each phase must produce an artifact containing:

- Status
- Approval Status
- Approver
- Approval Date
- Decisions
- Open Issues

Use:

`Approval Status: PENDING`

until the human explicitly approves the phase.

Do not assume approval from the existence of a file.

---

## Source of Truth

Approved SDLC artifacts are authoritative.

Do not contradict an approved artifact without explicitly requesting
human approval for the change.

---

## Code Modification Rules

Before modifying application code, verify:

- requirements.md exists and is approved
- architecture.md exists and is approved
- design-review.md exists and is approved
- implementation-plan.md exists and is approved

Do not modify application code during:

- Requirements
- Architecture
- Design Review
- Implementation Planning
- Code Review
- Verification
- PR preparation

Implementation is the only phase authorized to modify application code.

---

## Testing

New functionality must include appropriate automated tests.

Existing tests must remain passing unless a requirement explicitly changes
their expected behavior.

Verification owns the final test execution and verification report.

---

## Security

Never expose:

- passwords
- access tokens
- API keys
- OAuth credentials
- private keys
- secrets from MCP responses

Redact sensitive information from generated documentation.

---

## MCP

Use configured MCP integrations when required.

Possible integrations include:

- Jira
- Confluence
 

If an MCP integration is unavailable, use approved local artifacts where possible
and clearly report the limitation.

---

## Documentation

All phase artifacts belong in:

`docs/`

Use Markdown.

Do not create duplicate artifacts with different names.

---

## Human-in-the-Loop

Copilot may recommend, analyze, generate, and implement according to the
current phase.

Copilot must not silently advance across approval gates.

The human remains responsible for phase approval and final merge decisions.

---

## Existing Application

Preserve existing application behavior.

Prefer:

- minimal changes
- existing project patterns
- readable code
- small commits
- focused tests
- no unnecessary refactoring