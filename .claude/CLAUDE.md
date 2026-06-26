# GymLibrary — Claude Code Instructions

## Project Overview

Android exercise library app built as a portfolio project.
Full context in `docs/project-context.md`.

- **Package:** `com.miranda.gymlibrary`
- **Min SDK:** 26 (Android 8.0)
- **API:** ExerciseDB via RapidAPI (`https://exercisedb.p.rapidapi.com`)
- **Repo:** https://github.com/LucasFalcaoSilva/GymLibrary

## Stack

Kotlin · Jetpack Compose · Clean Architecture + MVVM · Koin · Retrofit +
OkHttp + Kotlinx Serialization · Coil (GIF support) · Coroutines + Flow ·
JUnit + MockK

## Architecture

Clean Architecture — 3 layers: `presentation → domain → data`.
Full diagram in `docs/architecture.md`.

```
com.miranda.gymlibrary
├── core/         # DI, network, theme, util
├── data/         # RepositoryImpl, RemoteDataSource, DTO, Mapper
├── domain/       # UseCase, Model, Repository (interface)
└── presentation/ # Screen, ViewModel, Navigation
```

## Before Any Implementation

1. Read `docs/architecture.md` and `docs/coding-conventions.md`
2. Read the target spec in `specs/` before writing any code
3. Never implement beyond the scope defined in the spec
4. Never implement logic from a spec that hasn't been read in this session

## Before Any Commit

1. Run `./gradlew assembleDebug` and confirm BUILD SUCCESSFUL
2. Use Conventional Commits — see `docs/coding-conventions.md`
3. One commit per spec implemented

## Code Review Rules

- Review scope is always limited to the spec being implemented
- Do NOT raise findings that belong to future specs
- Every finding must reference: file, line, severity, and the spec BR it violates (if any)
- Severities: 🔴 blocker · 🟡 high · ⚪ low
- Do NOT auto-fix findings — list them and wait for confirmation

## Key Business Rules (global)

- API key must never be hardcoded — always read from `BuildConfig.RAPIDAPI_KEY`
- `okhttp-logging` is `debugImplementation` — never `implementation`
- `HttpLoggingInterceptor` references must be guarded by `BuildConfig.DEBUG`
- No Android framework imports in `domain/` or `data/` layers
- Comments must explain WHY, never WHAT — self-explanatory code needs no comment

## Spec Index

| ID       | File                                          | Status |
|----------|-----------------------------------------------|--------|
| CORE-000 | specs/core/project-setup.md                  | ✅ Done |
| CORE-001 | specs/core/network-setup.md                  | ⏳ Next |
| CORE-002 | specs/core/di-setup.md                       | ⏳ Pending |
| CORE-003 | specs/core/navigation-setup.md               | ⏳ Pending |
| CORE-004 | specs/core/theme-setup.md                    | ⏳ Pending |
| FEAT-001 | specs/features/exercise-list/home-screen.md  | ⏳ Pending |
| FEAT-002 | specs/features/exercise-list/exercise-list-screen.md | ⏳ Pending |
| FEAT-003 | specs/features/exercise-detail/exercise-detail-screen.md | ⏳ Pending |
