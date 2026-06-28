# Error Handling — GymLibrary

This document defines the error handling strategy for all layers of the app.
All feature specs must follow these rules — no feature-specific error handling
deviates from this contract without explicit justification.

---

## Principles

- **Never expose technical errors to the user** — no HTTP codes, exception class names, stack traces, or raw API messages in the UI
- **Every error has a human-readable message** — always in Portuguese, always actionable
- **Errors propagate as `Result<T>`** from data layer to domain, and as `UiState.Error` from domain to presentation
- **Retry is always available** on error screens — the user must never be stuck

---

## Error Types

There are two distinct categories of errors in GymLibrary. They must **never be mixed**:

### 1. Infrastructure Errors

Errors caused by network, connectivity, or server failures. These are handled
by `Throwable.toUserMessage()` in `core/util/ErrorMapper.kt`.

**Use `toUserMessage()` for:**
- Initial screen load failures (full-screen `ErrorState`)
- Any `Result.failure` that comes from a network call on first load

| Category | Condition | Message (PT-BR) |
|---|---|---|
| No internet | `UnknownHostException`, `ConnectException` | "Sem conexão com a internet. Verifique sua rede e tente novamente." |
| Timeout | `SocketTimeoutException` | "A requisição demorou muito. Verifique sua conexão e tente novamente." |
| Server error | HTTP 5xx | "Serviço indisponível no momento. Tente novamente mais tarde." |
| Auth error | HTTP 401 / 403 | "Erro de autenticação. Verifique sua API key." |
| Not found | HTTP 404 | "Conteúdo não encontrado." |
| Client error | HTTP 4xx (outros) | "Não foi possível carregar os dados. Tente novamente." |
| Unknown | Qualquer outro erro | "Ocorreu um erro inesperado. Tente novamente." |

### 2. Operation Errors

Errors that occur during a specific user action after the screen has already
loaded (e.g. pagination, pull-to-refresh, form submission). These use
**fixed strings defined in the feature spec** — not `toUserMessage()`.

**Use fixed strings for:**
- Pagination load-more failures (Snackbar)
- Any secondary operation that doesn't replace the screen content

> The cause of the failure (network, timeout, server) is irrelevant to the user
> in these cases — they already have data on screen and just need to know the
> specific operation failed.

**Examples:**
- Pagination error → `"Erro ao carregar mais exercícios. Tente novamente."`
- Each feature spec defines its own operation error messages explicitly

---

## Error Propagation by Layer

### Data Layer (RemoteDataSource)

Wrap every API call in `runCatching` and return `Result<T>`:

```kotlin
suspend fun getBodyParts(): Result<List<String>> = runCatching {
    apiService.getBodyPartList()
}
```

Never catch and swallow exceptions — let them propagate wrapped in `Result.failure`.

### Domain Layer (UseCase)

Pass `Result<T>` through without modification — do not add business logic to errors:

```kotlin
class GetBodyPartsUseCase(private val repository: ExerciseRepository) {
    suspend operator fun invoke(): Result<List<String>> =
        repository.getBodyParts()
}
```

### Presentation Layer (ViewModel)

**Initial load (infrastructure error):**
```kotlin
viewModelScope.launch {
    _uiState.value = HomeUiState(uiState = UiState.Loading)
    useCase()
        .onSuccess { data ->
            _uiState.value = HomeUiState(uiState = UiState.Success(data))
        }
        .onFailure { error ->
            _uiState.value = HomeUiState(
                uiState = UiState.Error(error.toUserMessage())  // ← infrastructure
            )
        }
}
```

**Secondary operation (operation error):**
```kotlin
useCase()
    .onSuccess { /* append data */ }
    .onFailure {
        // Fixed string from spec — not toUserMessage()
        _snackbarMessage.value = "Erro ao carregar mais exercícios. Tente novamente."
    }
```

---

## Error Mapper

Create `core/util/ErrorMapper.kt` with a single extension function used by all ViewModels
**for infrastructure errors only**:

```kotlin
package com.miranda.gymlibrary.core.util

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserMessage(): String = when (this) {
    is UnknownHostException,
    is ConnectException ->
        "Sem conexão com a internet. Verifique sua rede e tente novamente."

    is SocketTimeoutException ->
        "A requisição demorou muito. Verifique sua conexão e tente novamente."

    is HttpException -> when (code()) {
        401, 403 -> "Erro de autenticação. Verifique sua API key."
        404      -> "Conteúdo não encontrado."
        in 500..599 -> "Serviço indisponível no momento. Tente novamente mais tarde."
        else     -> "Não foi possível carregar os dados. Tente novamente."
    }

    else -> "Ocorreu um erro inesperado. Tente novamente."
}
```

---

## UI Error State

Every screen must display full-screen infrastructure errors using the `ErrorState` composable:

```kotlin
is UiState.Error -> {
    ErrorState(
        message = state.uiState.message,
        onRetry = { viewModel.retry() }
    )
}
```

Extract this into a reusable composable `core/ui/components/ErrorState.kt`:

```kotlin
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
)
```

Operation errors (pagination, etc.) use `Snackbar` with the fixed string from the spec.

---

## Business Rules

- BR-01: HTTP status codes, exception class names, and stack traces must **never** appear in the UI
- BR-02: Every full-screen error state must offer a retry action
- BR-03: All user-facing error messages must be in **Portuguese**
- BR-04: `toUserMessage()` must only be used for **infrastructure errors** (initial load failures) — never for operation errors (pagination, secondary actions)
- BR-05: Operation error messages must be **fixed strings defined in the feature spec** — never derived from `toUserMessage()`
- BR-06: `runCatching` must wrap every API call in `RemoteDataSource` — never use bare `try/catch` that swallows exceptions
- BR-07: `ErrorState` composable from `core/ui/components/` must be used for full-screen errors — no inline error UI

---

## Files to create

- `core/util/ErrorMapper.kt` — `Throwable.toUserMessage()` extension
- `core/ui/components/ErrorState.kt` — reusable error composable

---

## Dependency

This document must be read before implementing any FEAT spec.
Add to `CLAUDE.md` under "Before Any Implementation".