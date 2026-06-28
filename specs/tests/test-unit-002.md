# Spec: Unit Tests — FEAT-002 + FEAT-003 (Exercise List + Detail)

**ID:** TEST-UNIT-002  
**Layer:** Unit Tests  
**Target specs:** FEAT-002, FEAT-003  
**Status:** Approved

## Description

Unit tests for `GetExercisesUseCase`, `ExerciseListViewModel`,
`GetExerciseDetailUseCase`, and `ExerciseDetailViewModel`.
All external dependencies mocked with MockK.

## Test Location

`app/src/test/java/com/miranda/gymlibrary/`

---

## GetExercisesUseCaseTest

**File:** `domain/usecase/GetExercisesUseCaseTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `invoke returns success when repository succeeds` | Repository returns exercise list | UseCase returns same `Result.success` |
| 2 | `invoke returns failure when repository fails` | Repository returns failure | UseCase propagates failure unchanged |
| 3 | `invoke passes correct parameters to repository` | Called with bodyPart/limit/offset | Repository called with exact same params |

---

## ExerciseListViewModelTest

**File:** `presentation/exerciselist/ExerciseListViewModelTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `initial state is Loading` | ViewModel created | `isLoading = true`, `exercises = emptyList()` |
| 2 | `emits exercises on success` | UseCase returns 20 exercises | `exercises` has 20 items, `isLoading = false` |
| 3 | `emits error on initial load failure` | UseCase returns `UnknownHostException` | `error` is non-null PT-BR message, `exercises` empty |
| 4 | `error message is human-readable` | Any infrastructure failure | `error` does not contain class names or HTTP codes |
| 5 | `retry clears error and reloads` | Error state → `retry()` → success | `exercises` populated, `error = null` |
| 6 | `loadMore appends exercises` | 20 loaded → `loadMore()` → 20 more | `exercises` has 40 items |
| 7 | `loadMore sets isLoadingMore` | `loadMore()` called | `isLoadingMore = true` during load, `false` after |
| 8 | `loadMore error shows snackbar with fixed string` | `loadMore()` fails | Snackbar message = `"Erro ao carregar mais exercícios. Tente novamente."` — NOT `toUserMessage()` |
| 9 | `loadMore does not replace existing exercises on error` | 20 loaded → `loadMore()` fails | `exercises` still has original 20 |
| 10 | `hasReachedEnd set when api returns fewer than limit` | API returns 5 items (< 20) | `hasReachedEnd = true` |
| 11 | `loadMore not triggered when hasReachedEnd is true` | `hasReachedEnd = true` → `loadMore()` | Repository NOT called again |

```kotlin
@Test
fun `loadMore error shows snackbar with fixed string`() = runTest {
    // initial load succeeds
    coEvery { useCase(any(), 20, 0) } returns Result.success(fakeExercises(20))
    // load more fails
    coEvery { useCase(any(), 20, 20) } returns Result.failure(IOException())

    viewModel.loadExercises("back")
    advanceUntilIdle()
    viewModel.loadMore()
    advanceUntilIdle()

    assertEquals(
        "Erro ao carregar mais exercícios. Tente novamente.",
        viewModel.uiState.value.paginationError
    )
    assertEquals(20, viewModel.uiState.value.exercises.size)
}
```

---

## GetExerciseDetailUseCaseTest

**File:** `domain/usecase/GetExerciseDetailUseCaseTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `invoke returns success when repository succeeds` | Repository returns `Exercise` | UseCase returns same `Result.success` |
| 2 | `invoke returns failure when repository fails` | Repository returns failure | UseCase propagates failure |
| 3 | `invoke delegates to repository with correct id` | Called with `exerciseId = "0007"` | Repository called with same id |

---

## ExerciseDetailViewModelTest

**File:** `presentation/exercisedetail/ExerciseDetailViewModelTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `initial state is Loading` | ViewModel created | `uiState = UiState.Loading` |
| 2 | `emits Success with exercise data` | UseCase returns exercise | `uiState = UiState.Success(exercise)` |
| 3 | `emits Error on network failure` | UseCase returns `UnknownHostException` | `uiState = UiState.Error` with PT-BR message |
| 4 | `emits Error on HTTP 404` | UseCase returns `HttpException(404)` | `uiState = UiState.Error("Conteúdo não encontrado.")` |
| 5 | `error message never exposes HTTP code` | Any HTTP failure | `message` does not contain "404", "500", etc. |
| 6 | `retry reloads exercise` | Error → `retry()` → success | `uiState = UiState.Success` |

## Business Rules Validated

| BR | Source | Test |
|---|---|---|
| BR-01 | FEAT-002 | Test #2 — pagination limit=20 |
| BR-04 | error-handling.md | Tests #3, #8 — toUserMessage() for infrastructure, fixed string for operation |
| BR-05 | error-handling.md | Tests #3, #8 — runCatching propagates correctly |
| FEAT-003 BR-04 | exercise-detail-screen.md | Test ExerciseDetail #4 — HTTP 404 maps to "Conteúdo não encontrado." |

## Files to create

- `domain/usecase/GetExercisesUseCaseTest.kt`
- `presentation/exerciselist/ExerciseListViewModelTest.kt`
- `domain/usecase/GetExerciseDetailUseCaseTest.kt`
- `presentation/exercisedetail/ExerciseDetailViewModelTest.kt`

## Note

`MainDispatcherRule` is shared — created in TEST-UNIT-001 at `util/MainDispatcherRule.kt`.
