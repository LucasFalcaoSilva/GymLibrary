# Architecture — GymLibrary

## Pattern

Clean Architecture + MVVM, single-module.

## Layer Diagram

```
┌─────────────────────────────────────┐
│          Presentation Layer         │
│   Composables · ViewModel · State   │
└────────────────┬────────────────────┘
                 │ uses
┌────────────────▼────────────────────┐
│            Domain Layer             │
│    UseCase · Model · Repository     │
│            (interfaces)             │
└────────────────┬────────────────────┘
                 │ implements
┌────────────────▼────────────────────┐
│             Data Layer              │
│  RepositoryImpl · RemoteDataSource  │
│       ApiService · DTO · Mapper     │
└─────────────────────────────────────┘
```

## Package Structure

```
com.miranda.gymlibrary
├── core
│   ├── di/                  # Koin modules
│   ├── network/             # Retrofit setup, interceptors
│   ├── ui/theme/            # MaterialTheme, colors, typography
│   └── util/                # Extensions, constants
├── data
│   ├── remote/
│   │   ├── api/             # ExerciseDbService (Retrofit interface)
│   │   ├── dto/             # Response DTOs
│   │   └── datasource/      # RemoteDataSourceImpl
│   ├── mapper/              # DTO → Domain model
│   └── repository/          # RepositoryImpl
├── domain
│   ├── model/               # Exercise, BodyPart, Equipment
│   ├── repository/          # Repository interface
│   └── usecase/             # GetBodyPartsUseCase, GetExercisesUseCase,
│                            # GetExerciseDetailUseCase
└── presentation
    ├── navigation/          # NavGraph, Routes
    ├── home/                # HomeScreen, HomeViewModel
    ├── exerciselist/        # ExerciseListScreen, ExerciseListViewModel
    └── exercisedetail/      # ExerciseDetailScreen, ExerciseDetailViewModel
```

## Navigation Flow

```
HomeScreen
    │
    └──► ExerciseListScreen (bodyPart)
              │
              └──► ExerciseDetailScreen (exerciseId)
```

## State Management

Every screen uses a sealed `UiState`:

```kotlin
sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

## Dependency Injection

Koin with the following modules:

- `networkModule` — Retrofit, OkHttp, ExerciseDbService
- `dataModule` — RemoteDataSource, Repository
- `domainModule` — UseCases
- `presentationModule` — ViewModels

## Dependencies

```toml
[versions]
kotlin = "2.0.0"
compose-bom = "2024.06.00"
koin = "3.5.6"
retrofit = "2.11.0"
okhttp = "4.12.0"
coil = "2.6.0"
coroutines = "1.8.1"

[libraries]
# Compose
compose-bom
compose-ui
compose-material3
compose-navigation

# Network
retrofit-core
retrofit-kotlinx-serialization
okhttp-logging-interceptor
kotlinx-serialization-json

# DI
koin-android
koin-compose

# Image loading
coil-compose
coil-gif               # required for GIF support

# Async
coroutines-android
```
