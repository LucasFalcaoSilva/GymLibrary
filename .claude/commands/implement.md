# /implement — Implement a Spec

Implement a spec from `specs/` following SDD workflow.

## Usage

```
/implement CORE-001
```

## Steps

1. Read `CLAUDE.md` for project context and global rules
2. Read `docs/architecture.md` for layer and package structure
3. Read `docs/coding-conventions.md` for naming and patterns
4. Read the target spec file from `specs/`
5. Identify all files to create or modify per the spec
6. Implement exactly what the spec defines — no more, no less
7. Run `./gradlew assembleDebug` and confirm BUILD SUCCESSFUL
8. Read `.claude/commands/review.md` and execute its steps to validate the implementation
9. If review passes with no 🔴 findings, proceed to step 10
10. Read `.claude/commands/report.md` and execute its steps to generate the implementation report in `reports/[SPEC-ID].md`
11. If the spec is a FEAT (not CORE): notify the developer to run `/qa [SPEC-ID]` before committing
12. Stage files and report ready for `/qa` (FEAT specs) or `/commit` (CORE specs)

## Rules

- Never implement logic from a spec not read in this session
- Never implement features from future specs, even if they seem related
- If a decision is not covered by the spec, stop and ask before proceeding
- TODOs left by previous specs must remain untouched unless the current spec explicitly covers them
- Every new file must have the correct package declaration matching `docs/architecture.md`
