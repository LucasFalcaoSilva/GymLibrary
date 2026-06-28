# QA Test Plan — FEAT-003 Exercise Detail Screen

**Feature:** Exercise Detail  
**Spec:** specs/features/exercise-detail/exercise-detail-screen.md  
**Environment:** Physical device or emulator, API 26+  
**Prerequisites:** App installed, valid `RAPIDAPI_KEY`, FEAT-001 and FEAT-002 passing

---

## TC-001 — Happy Path: Exercise detail loads successfully

**Steps:**
1. Launch the app
2. Tap any body part on Home Screen
3. Tap any exercise card on Exercise List Screen

**Expected:**
- [ ] Loading indicator appears while fetching
- [ ] GIF renders in full width
- [ ] Exercise name appears capitalized as headline
- [ ] Two chips visible: one for `bodyPart`, one for `equipment`
- [ ] "Músculos Ativados" section visible with primary muscle
- [ ] Secondary muscles listed separated by comma
- [ ] "Instruções" section visible with numbered steps starting at 1
- [ ] Top app bar shows exercise name + back button

---

## TC-002 — Visual: GIF placeholder

**Steps:**
1. Navigate to any exercise detail on a slow connection

**Expected:**
- [ ] Placeholder color (`surface`) shows while GIF loads
- [ ] GIF appears after loading without layout shift
- [ ] No broken image icon visible

---

## TC-003 — Visual: Chips

**Steps:**
1. Navigate to any exercise detail

**Expected:**
- [ ] `bodyPart` chip uses `AssistChip` Material3 component
- [ ] `equipment` chip uses `AssistChip` Material3 component
- [ ] Both chips are readable and not clipped

---

## TC-004 — Content: Instructions

**Steps:**
1. Navigate to any exercise detail
2. Scroll to the Instructions section

**Expected:**
- [ ] Instructions are numbered starting at 1
- [ ] Each step is readable and not truncated
- [ ] Scroll works smoothly through all steps

---

## TC-005 — Error State: No network on initial load

**Steps:**
1. Disable Wi-Fi and mobile data
2. Navigate to any exercise detail from Exercise List

**Expected:**
- [ ] Loading indicator appears briefly
- [ ] Full-screen error message appears (human-readable, in Portuguese)
- [ ] No HTTP code or exception class name visible
- [ ] "Tentar novamente" button is visible
- [ ] No crash occurs

---

## TC-006 — Retry: Recover from error state

**Steps:**
1. Reproduce TC-005 (no network)
2. Re-enable Wi-Fi
3. Tap "Tentar novamente"

**Expected:**
- [ ] Loading indicator appears again
- [ ] Exercise detail loads successfully

---

## TC-007 — Back Navigation

**Steps:**
1. Navigate to any exercise detail
2. Tap the back button in the top app bar

**Expected:**
- [ ] App navigates back to Exercise List Screen
- [ ] Exercise List Screen still shows the previously loaded exercises
- [ ] No crash occurs

---

## TC-008 — Full Flow: Home → List → Detail → Back → Back

**Steps:**
1. Open the app
2. Tap a body part on Home
3. Tap an exercise on the list
4. View the detail
5. Tap back → Exercise List
6. Tap back → Home

**Expected:**
- [ ] Full navigation flow works without crash
- [ ] Each screen maintains its state correctly
- [ ] No duplicate screens or unexpected back stack behavior

---

## Sign-off

| TC | Result | Notes |
|----|--------|-------|
| TC-001 | ✅ Pass  | |
| TC-002 | ✅ Pass  | |
| TC-003 | ✅ Pass  | |
| TC-004 | ✅ Pass  | |
| TC-005 | ✅ Pass  | |
| TC-006 | ✅ Pass  | |
| TC-007 | ✅ Pass  | |
| TC-008 | ✅ Pass  | |

**QA Status:** ✅ Approved
**Tested by:** Lucas  
**Date:** 2026-06-28  
**Device/Emulator:** Medium Phone
**Android version:** 16