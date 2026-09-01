# Pre-Implementation Hook

## Purpose

Ensure that implementation cannot begin until the required SDLC
artifacts have been completed and explicitly approved.

## Trigger

This governance check applies before application source code is created
or modified during the Implementation phase.

## Required Artifacts

The following documents must exist:

- `docs/requirements.md`
- `docs/architecture.md`
- `docs/design-review.md`
- `docs/implementation-plan.md`

## Approval Requirement

Implementation may begin only when all required artifacts contain an
explicit human approval status.

Expected approval markers:

- `Status: APPROVED`
- or an equivalent clearly defined approval section.

## Required Order

The following order must be respected:

1. Requirements approved
2. Architecture approved
3. Design Review approved
4. Implementation Plan approved
5. Implementation begins

## Enforcement

If any required artifact is missing or not approved:

- deny the source-code modification
- identify the missing approval
- instruct the agent to stop implementation
- do not modify application source files

## Allowed Actions Before Approval

The agent may:

- inspect files
- analyze requirements
- update SDLC documentation when explicitly requested
- ask the human for clarification
- prepare recommendations

The agent must not:

- modify production/application source code
- introduce unapproved functionality
- bypass an approval gate
- silently mark an artifact as approved

## Human-in-the-Loop Rule

GitHub Copilot must never approve its own SDLC artifact.

Approval must come from the human reviewer.