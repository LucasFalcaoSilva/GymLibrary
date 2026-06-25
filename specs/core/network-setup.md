# Spec: Network Setup

**ID:** CORE-001  
**Layer:** Core / Network  
**Status:** Approved

## Description

Configure Retrofit + OkHttp to communicate with the ExerciseDB API on RapidAPI.

## Preconditions

- `RAPIDAPI_KEY` defined in `local.properties`
- `local.properties` listed in `.gitignore`

## Behavior

1. Retrofit instance uses base URL `https://exercisedb.p.rapidapi.com`
2. Every request includes headers:
   - `X-RapidAPI-Key: <RAPIDAPI_KEY>`
   - `X-RapidAPI-Host: exercisedb.p.rapidapi.com`
3. Headers are injected via OkHttp `Interceptor`, not per-call
4. JSON deserialization uses `kotlinx.serialization`
5. `HttpLoggingInterceptor` is active only on `BuildConfig.DEBUG`
6. Timeout: connect 10s, read 30s, write 15s

## Files to create

- `core/network/NetworkModule.kt` — Koin module with Retrofit + OkHttp
- `core/network/AuthInterceptor.kt` — injects RapidAPI headers
- `BuildConfig` field `RAPIDAPI_KEY` read from `local.properties`

## Business Rules

- BR-01: Key must never be hardcoded in source files
- BR-02: Logging interceptor must not run in release builds

## Out of Scope

- Caching
- Certificate pinning
