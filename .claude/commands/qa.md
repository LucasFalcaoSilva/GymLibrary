# /qa — Execute QA Test Plan

Display the QA test plan for a feature and register the sign-off result in the implementation report.

## Usage

```
/qa FEAT-001
```

## Steps

1. Read `docs/qa/qa-[FEAT-ID].md`
2. Display each test case one by one, waiting for confirmation of Pass or Fail on each
3. After all test cases are executed:
   - If all Pass → append `## QA Sign-off` section to `reports/[FEAT-ID].md` with status **Approved**
   - If any Fail → append `## QA Sign-off` section with status **Blocked** and list the failed TCs
4. Display final QA status clearly:
   - ✅ QA Approved — ready for `/commit [FEAT-ID]`
   - ❌ QA Blocked — fix failing TCs before committing

## QA Sign-off Section (appended to reports/FEAT-ID.md)

```markdown
## QA Sign-off

**Status:** ✅ Approved / ❌ Blocked  
**Date:** YYYY-MM-DD  
**Device/Emulator:**  
**Android version:**  

| TC | Description | Result |
|----|-------------|--------|
| TC-001 | Happy Path: Body Part Grid loads | ✅ Pass / ❌ Fail |
| TC-002 | Navigation: Tap a body part card | ✅ Pass / ❌ Fail |
| TC-003 | Error State: No network | ✅ Pass / ❌ Fail |
| TC-004 | Retry: Recover from error | ✅ Pass / ❌ Fail |
| TC-005 | Visual: Theme and layout | ✅ Pass / ❌ Fail |

**Failed TCs:** [list or "None"]  
**Notes:** [any observations]
```

## Rules

- Never mark QA as Approved if any TC failed
- Never skip TCs — all must be executed
- If the QA test plan file does not exist for the feature, abort and notify:
  `❌ QA test plan not found — create docs/qa/qa-[FEAT-ID].md first`
