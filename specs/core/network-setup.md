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

- `core/di/NetworkModule.kt` — Koin module with Retrofit + OkHttp
- `core/network/AuthInterceptor.kt` — injects RapidAPI headers
- `BuildConfig` field `RAPIDAPI_KEY` read from `local.properties`

## Business Rules

- BR-01: Key must never be hardcoded in source files
- BR-02: `HttpLoggingInterceptor` must only be added to the OkHttp client when `BuildConfig.DEBUG` is true — the class is `debugImplementation` only and will cause an "Unresolved reference" compile error in release builds if referenced unconditionally. Use:
  ```kotlin
  if (BuildConfig.DEBUG) {
      addInterceptor(HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
      })
  }
  ```
  Alternatively, place the reference inside `src/debug/` source set.
- BR-03: On startup, assert that `RAPIDAPI_KEY` is not blank before building the OkHttpClient:
  ```kotlin
  check(BuildConfig.RAPIDAPI_KEY.isNotBlank()) {
      "RAPIDAPI_KEY not set — add it to local.properties"
  }
  ```
  Without this guard, an unconfigured key sends `X-RapidAPI-Key: ` (empty string) on every request, returning 401/403 with no local diagnostic.

## Out of Scope

- Caching
- Certificate pinning
