# /qa — Validate QA Test Plan and Register Sign-off

Read the completed QA test plan, validate results, and register the sign-off in the implementation report.

## Usage

```
/qa FEAT-001
```

## Prerequisites

Before running this command, you must:
1. Open `docs/qa/qa-[FEAT-ID].md`
2. Install and run the app on a device or emulator
3. Execute each TC manually and fill in Pass/Fail in the Sign-off table
4. Fill in device, Android version, and date fields

## Steps

1. Read `docs/qa/qa-[FEAT-ID].md`
2. Check that all TCs have a result (Pass or Fail) — if any is blank, abort:
   ```
   ❌ QA incompleto — TC-00X sem resultado.
   Preencha todos os TCs em docs/qa/qa-[FEAT-ID].md antes de continuar.
   ```
3. Check if any TC failed:
    - All Pass → status = ✅ Approved
    - Any Fail → status = ❌ Blocked
4. Append `## QA Sign-off` section to `reports/[FEAT-ID].md`
5. Display final result:
    - ✅ `QA Approved — pode rodar /commit [FEAT-ID]`
    - ❌ `QA Blocked — corrija os TCs com falha antes de commitar`

## QA Sign-off Section (appended to reports/FEAT-ID.md)

```markdown
## QA Sign-off

**Status:** ✅ Approved / ❌ Blocked
**Date:** YYYY-MM-DD
**Device/Emulator:** [from test plan]
**Android version:** [from test plan]

| TC | Description | Result |
|----|-------------|--------|
| TC-001 | description | ✅ Pass / ❌ Fail |

**Failed TCs:** [list or "None"]
**Notes:** [any observations from test plan]
```

## Rules

- Never mark QA as Approved if any TC failed
- Never skip validation — all TCs must have a result before registering
- If `docs/qa/qa-[FEAT-ID].md` does not exist → abort:
  ```
  ❌ Test plan não encontrado — crie docs/qa/qa-[FEAT-ID].md primeiro.
  ```