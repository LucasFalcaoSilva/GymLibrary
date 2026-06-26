# /review — Scoped Code Review

Perform a code review strictly scoped to the spec provided as argument.

## Usage

```
/review CORE-001
```

## Steps

1. Read the target spec file from `specs/` using the ID provided
2. Read `docs/coding-conventions.md`
3. Identify all files created or modified by that spec
4. Review those files against:
   - Every BR defined in the spec
   - Every acceptance criterion defined in the spec
   - Global rules in `docs/coding-conventions.md`
   - Global rules in `CLAUDE.md`
5. After the review, update `reports/SPEC-ID.md`:
   - If the file exists → append a new `## Review Round N` section at the end
   - If the file does not exist → create it with only the review section
   - Increment the round number based on how many review sections already exist

## Scope Rules

- ONLY raise findings that relate to the spec provided
- If a finding belongs to a future spec, mark it as `[OUT OF SCOPE — belongs to SPEC-ID]` and do not include it in the findings list
- Do NOT auto-fix anything — list findings and wait for confirmation

## Output Format

```
CODE REVIEW — [SPEC-ID]
Build: ./gradlew assembleDebug → [result]
Spec BRs: [list each BR and ✅/❌ status]

FINDINGS
🔴 [CONFIRMED] file:line — description
   Current code: ...
   Fix: ...
   BR violated: BR-XX

🟡 [PLAUSIBLE] file:line — description
   ...

OUT OF SCOPE (not included in findings)
- file:line — description [belongs to CORE-00X]

PRIORITY
AGORA     → findings to fix before commit
NEXT SPEC → findings deferred by design
```

## Report Section Format (appended to reports/SPEC-ID.md)

```markdown
## Review Round N

**Date:** YYYY-MM-DD  
**Build:** ✅ BUILD SUCCESSFUL / ❌ BUILD FAILED  
**Triggered by:** /implement flow / standalone /review  

### BR Compliance

| BR | Description | Status |
|----|-------------|--------|
| BR-01 | description | ✅ / ❌ |

### Findings

| # | Severity | File:Line | Description | Status |
|---|----------|-----------|-------------|--------|
| 1 | 🔴/🟡/⚪ | file:line | description | Fixed before commit / Deferred to SPEC-ID / Out of scope |

If no findings: "No findings — all BRs passed."

### Out of Scope

| File:Line | Description | Target Spec |
|-----------|-------------|-------------|
| file:line | description | SPEC-ID |

If nothing out of scope: "None."
```