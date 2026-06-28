# QA Test Plan — FEAT-001 Home Screen

**Feature:** Home Screen — Body Part Grid  
**Spec:** specs/features/exercise-list/home-screen.md  
**Environment:** Physical device or emulator, API 26+  
**Prerequisites:** App installed, valid `RAPIDAPI_KEY` in `local.properties`

---

## TC-001 — Happy Path: Body Part Grid loads successfully

**Steps:**
1. Launch the app
2. Wait for the home screen to load

**Expected:**
- [ ] Loading indicator appears while fetching
- [ ] Grid renders with 2 columns
- [ ] All body part cards are visible
- [ ] Each card shows a representative icon
- [ ] Each card shows the body part name capitalized (e.g. `"back"` → `"Back"`)
- [ ] Order matches the API response order

---

## TC-002 — Navigation: Tap a body part card

**Steps:**
1. Wait for the grid to load
2. Tap any body part card

**Expected:**
- [ ] App navigates away from Home Screen
- [ ] No crash occurs

---

## TC-003 — Error State: No network connection

**Steps:**
1. Disable Wi-Fi and mobile data
2. Launch the app (or kill and relaunch)

**Expected:**
- [ ] Loading indicator appears briefly
- [ ] Error message is displayed
- [ ] "Tentar novamente" / retry button is visible
- [ ] No crash occurs

---

## TC-004 — Retry: Recover from error state

**Steps:**
1. Reproduce TC-003 (no network)
2. Re-enable Wi-Fi
3. Tap the retry button

**Expected:**
- [ ] Loading indicator appears again
- [ ] Grid loads successfully
- [ ] No duplicate cards appear

---

## TC-005 — Visual: Theme and layout

**Steps:**
1. Load the home screen successfully

**Expected:**
- [ ] Dark background (`#121212`)
- [ ] Primary red color (`#E53935`) applied to interactive elements
- [ ] No text overflow on card labels
- [ ] Cards are evenly spaced in the 2-column grid
- [ ] Top app bar displays the app name

---

## Sign-off

| TC | Result  | Notes |
|----|---------|-------|
| TC-001 | ✅ Pass  | |
| TC-002 | ✅ Pass  | |
| TC-003 | ✅ Pass  | |
| TC-004 | ✅ Pass  | |
| TC-005 | ✅ Pass  | |

**QA Status:** ✅ Approved
**Tested by:** Lucas  
**Date:** 2026-06-28  
**Device/Emulator:** Medium Phone 
**Android version:** 16
