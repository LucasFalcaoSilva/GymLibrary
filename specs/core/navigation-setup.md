# Spec: Navigation Setup

**ID:** CORE-003  
**Layer:** Core / Navigation  
**Status:** Approved

## Description

Configure Jetpack Compose Navigation with typed routes for the three screens.

## Routes

| Route Object | Path | Arguments |
|---|---|---|
| `HomeRoute` | `home` | none |
| `ExerciseListRoute` | `exercise-list/{bodyPart}` | `bodyPart: String` |
| `ExerciseDetailRoute` | `exercise-detail/{exerciseId}` | `exerciseId: String` |

## Behavior

1. `NavHost` starts at `HomeRoute`
2. `HomeScreen` navigates to `ExerciseListRoute` passing selected `bodyPart`
3. `ExerciseListScreen` navigates to `ExerciseDetailRoute` passing `exerciseId`
4. Both `ExerciseListScreen` and `ExerciseDetailScreen` have a back arrow in the top app bar

## Files to create

- `presentation/navigation/Routes.kt` — route objects with path constants
- `presentation/navigation/NavGraph.kt` — `NavHost` composable with all destinations

## Business Rules

- BR-01: `bodyPart` must be URL-encoded before passing as nav argument
- BR-02: NavGraph must not reference ViewModel or UseCase directly
