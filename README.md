# GymLibrary

An Android exercise library app built as a portfolio project to demonstrate Specification-Driven Development (SDD), Clean Architecture, and Jetpack Compose.

## Features

- Browse gym exercises by muscle group
- View animated GIF demonstrations
- See primary and secondary muscles activated
- Step-by-step instructions for each exercise
- Paginated exercise list
- Human-readable error messages with retry
- Dark theme with gym-inspired color palette

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Architecture:** Clean Architecture + MVVM
- **DI:** Koin
- **Network:** Retrofit + OkHttp + Kotlinx Serialization
- **Image Loading:** Coil (with GIF support)
- **Async:** Coroutines + Flow
- **Testing:** JUnit 4 + MockK + MockWebServer

## API

[ExerciseDB v2.2 via RapidAPI](https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb)

## Getting Started

1. Get a free API key at [RapidAPI](https://rapidapi.com)
2. Add to `local.properties`:
```
RAPIDAPI_KEY=your_key_here
```
3. Run the project

## Architecture

```
presentation/ → domain/ → data/
     ↓              ↓         ↓
ViewModel      UseCase    Repository
Composable     Model      RemoteDataSource
               Repository API Service
               (interface) DTO + Mapper
```

Full architecture documentation in [`docs/architecture.md`](docs/architecture.md).

## Documentation

| Doc | Description |
|---|---|
| [`docs/architecture.md`](docs/architecture.md) | Layer diagram, package structure, navigation flow |
| [`docs/coding-conventions.md`](docs/coding-conventions.md) | Naming, commits, error handling patterns |
| [`docs/error-handling.md`](docs/error-handling.md) | Infrastructure vs operation errors, ErrorMapper |
| [`docs/qa/`](docs/qa/) | QA test plans per feature |

## Specs

This project was built following SDD — all specs are documented before code is written.

### Core

| ID | Spec | Status |
|---|---|---|
| CORE-000 | Project Setup | ✅ Done |
| CORE-001 | Network Setup | ✅ Done |
| CORE-002 | DI Setup | ✅ Done |
| CORE-003 | Navigation Setup | ✅ Done |
| CORE-004 | Theme Setup | ✅ Done |

### Features

| ID | Spec | Status |
|---|---|---|
| FEAT-001 | Home — Body Parts | ✅ Done |
| FEAT-002 | Exercise List | ✅ Done |
| FEAT-003 | Exercise Detail | ✅ Done |

### Bug Fixes

| ID | Spec | Status |
|---|---|---|
| BUG-001 | Back Stack Corrompido | ⏳ Pending |
| BUG-002 | GIF Exibindo como Imagem Estática | ⏳ Pending |
| BUG-003 | Paginação Não Carregando | ⏳ Pending |

### Improvements

| ID | Spec | Status |
|---|---|---|
| IMP-001 | Cache Local com Room | ⏳ Pending |
| IMP-002 | App Icon + Home Title | ⏳ Pending |
| IMP-003 | Internacionalização (PT-BR + EN) | ⏳ Pending |

### Tests

| ID | Spec | Status |
|---|---|---|
| TEST-UNIT-001 | Unit Tests — Home Screen | ⏳ Pending |
| TEST-UNIT-002 | Unit Tests — Exercise List + Detail | ⏳ Pending |
| TEST-INTEG-001 | Integration Tests — Repository + MockWebServer | ⏳ Pending |

## Reports

Implementation reports for each spec are available in [`reports/`](reports/).
Each report documents files created, BR compliance, review findings per round, and deviations from spec.

## License

MIT License — feel free to use this project as a reference.