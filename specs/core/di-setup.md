# Spec: DI Setup

**ID:** CORE-002  
**Layer:** Core / DI  
**Status:** Approved

## Description

Configure Koin as the dependency injection framework for the app.

## Behavior

1. Koin is started in `Application.onCreate()`
2. Modules loaded: `networkModule`, `dataModule`, `domainModule`, `presentationModule`
3. ViewModels declared with `viewModel {}` block in `presentationModule`
4. UseCases declared with `factory {}` (new instance per injection)
5. Repository and DataSource declared with `single {}` (singleton)

## Files to create

- `GymLibraryApplication.kt` — Application class with `startKoin {}`
- `core/di/NetworkModule.kt`
- `core/di/DataModule.kt`
- `core/di/DomainModule.kt`
- `core/di/PresentationModule.kt`

## Out of Scope

- Scoped Koin modules
- Koin test utilities (belongs in test specs)
