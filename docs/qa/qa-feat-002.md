# QA Test Plan — FEAT-002 Exercise List Screen

**Feature:** Exercise List — Paginated Exercise Grid  
**Spec:** specs/features/exercise-list/exercise-list-screen.md  
**Environment:** Physical device or emulator, API 26+  
**Prerequisites:** App installed, valid `RAPIDAPI_KEY`, FEAT-001 passing

---

## TC-001 — Happy Path: Exercise list loads successfully

**Steps:**
1. Launch the app
2. Wait for the Home Screen to load
3. Tap any body part card (e.g. "Back")

**Expected:**
- [ ] Loading indicator appears while fetching
- [ ] List renders with exercise cards
- [ ] Each card shows: animated GIF (80dp) + exercise name + target muscle
- [ ] Exercise names are capitalized
- [ ] Top app bar shows the selected body part capitalized + back button

---

## TC-002 — Pagination: Load more exercises on scroll

**Steps:**
1. Navigate to any exercise list
2. Wait for the first 20 exercises to load
3. Scroll to the bottom of the list

**Expected:**
- [ ] Loading indicator appears at the bottom
- [ ] More exercises are appended to the list
- [ ] No existing exercises disappear or reorder
- [ ] Total count increases by up to 20

---

## TC-003 — Navigation: Tap an exercise card

**Steps:**
1. Navigate to any exercise list
2. Wait for the list to load
3. Tap any exercise card

**Expected:**
- [ ] App navigates to Exercise Detail Screen
- [ ] No crash occurs

---

## TC-004 — Error State: No network on initial load

**Steps:**
1. Disable Wi-Fi and mobile data
2. Navigate to any exercise list from Home

**Expected:**
- [ ] Loading indicator appears briefly
- [ ] Full-screen error message appears (human-readable, in Portuguese)
- [ ] No HTTP code or exception class name visible
- [ ] "Tentar novamente" button is visible
- [ ] No crash occurs

---

## TC-005 — Retry: Recover from initial load error

**Steps:**
1. Reproduce TC-004 (no network)
2. Re-enable Wi-Fi
3. Tap "Tentar novamente"

**Expected:**
- [ ] Loading indicator appears again
- [ ] Exercise list loads successfully
- [ ] No duplicate cards appear

---

## TC-006 — Pagination Error: Snackbar on load-more failure

**Steps:**
1. Load the exercise list successfully (first 20)
2. Disable Wi-Fi
3. Scroll to the bottom to trigger load-more

**Expected:**
- [ ] Snackbar appears with the message: "Erro ao carregar mais exercícios. Tente novamente."
- [ ] Existing exercises remain visible — list is not replaced
- [ ] No full-screen error appears
- [ ] No crash occurs

---

## TC-007 — Back Navigation

**Steps:**
1. Navigate to any exercise list
2. Tap the back button in the top app bar

**Expected:**
- [ ] App navigates back to Home Screen
- [ ] No crash occurs

---

## TC-008 — Visual: Theme and layout

**Steps:**
1. Load the exercise list successfully

**Expected:**
- [ ] Dark background applied
- [ ] GIF placeholder visible while images load
- [ ] No text overflow on exercise names
- [ ] Cards are evenly spaced in the vertical list

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