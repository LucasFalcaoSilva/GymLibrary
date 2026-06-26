# GymLibrary

An Android exercise library app built as a portfolio project to demonstrate Specification-Driven Development (SDD), Clean Architecture, and Jetpack Compose.

## Features

- Browse gym exercises by muscle group
- View animated GIF demonstrations
- See primary and secondary muscles activated
- Step-by-step instructions for each exercise
- Paginated exercise list

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Architecture:** Clean Architecture + MVVM
- **DI:** Koin
- **Network:** Retrofit + OkHttp + Kotlinx Serialization
- **Image Loading:** Coil (with GIF support)
- **Async:** Coroutines + Flow

## API

[ExerciseDB via RapidAPI](https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb)

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

## Specs

This project was built following SDD — all specs are documented before code is written.

| ID | Spec | Status |
|---|---|---|
| CORE-000 | Project Setup | ✅ Done |
| CORE-001 | Network Setup | ⏳ Next |
| CORE-002 | DI Setup | ⏳ Pending |
| CORE-003 | Navigation Setup | ⏳ Pending |
| CORE-004 | Theme Setup | ⏳ Pending |
| FEAT-001 | Home — Body Parts | ⏳ Pending |
| FEAT-002 | Exercise List | ⏳ Pending |
| FEAT-003 | Exercise Detail | ⏳ Pending |

## License

MIT License — feel free to use this project as a reference.