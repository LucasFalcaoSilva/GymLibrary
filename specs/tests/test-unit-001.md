# Spec: Unit Tests — FEAT-001 (Home Screen)

**ID:** TEST-UNIT-001  
**Layer:** Unit Tests  
**Target specs:** FEAT-001  
**Status:** Approved

## Description

Unit tests for `GetBodyPartsUseCase` and `HomeViewModel`. All external dependencies
are mocked with MockK. No Android framework, no network, no coroutine dispatcher issues.

## Dependencies

```toml
# Already in libs.versions.toml
junit = "4.13.2"
mockk = "1.13.11"
coroutines-test = same version as coroutines-android
```

## Test Location

`app/src/test/java/com/miranda/gymlibrary/`

---

## GetBodyPartsUseCaseTest

**File:** `domain/usecase/GetBodyPartsUseCaseTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `invoke returns success when repository succeeds` | Repository returns `Result.success(listOf("back", "chest"))` | UseCase returns same `Result.success` |
| 2 | `invoke returns failure when repository fails` | Repository returns `Result.failure(IOException())` | UseCase returns same `Result.failure` |
| 3 | `invoke delegates to repository` | Any call | `repository.getBodyParts()` called exactly once |

```kotlin
@Test
fun `invoke returns success when repository succeeds`() = runTest {
    val bodyParts = listOf("back", "chest", "legs")
    coEvery { repository.getBodyParts() } returns Result.success(bodyParts)

    val result = useCase()

    assertTrue(result.isSuccess)
    assertEquals(bodyParts, result.getOrNull())
}
```

---

## HomeViewModelTest

**File:** `presentation/home/HomeViewModelTest.kt`

| # | Test | Scenario | Expected |
|---|------|----------|----------|
| 1 | `initial state is Loading` | ViewModel created | `uiState` is `UiState.Loading` |
| 2 | `emits Success when use case returns data` | UseCase returns body parts list | `uiState` transitions to `UiState.Success` with data |
| 3 | `emits Error when use case fails with network error` | UseCase returns `UnknownHostException` | `uiState` transitions to `UiState.Error` with PT-BR message |
| 4 | `emits Error when use case fails with timeout` | UseCase returns `SocketTimeoutException` | `uiState` is `UiState.Error` with timeout message |
| 5 | `emits Error when use case fails with HTTP 401` | UseCase returns `HttpException(401)` | `uiState` is `UiState.Error` with auth message |
| 6 | `retry reloads body parts after error` | Error state → `retry()` called → UseCase succeeds | `uiState` transitions Loading → Error → Loading → Success |
| 7 | `error message is never a raw exception or HTTP code` | Any failure | `uiState.message` does not contain class name or HTTP code |

```kotlin
@Test
fun `emits Success when use case returns data`() = runTest {
    val bodyParts = listOf("back", "chest")
    coEvery { useCase() } returns Result.success(bodyParts)

    viewModel.loadBodyParts()
    advanceUntilIdle()

    val state = viewModel.uiState.value.uiState
    assertTrue(state is UiState.Success)
    assertEquals(bodyParts, (state as UiState.Success).data)
}
```

## Business Rules Validated

| BR | Source | Test |
|---|---|---|
| BR-01 | error-handling.md | Test #7 — no raw exception message |
| BR-02 | error-handling.md | Tests #3–5 — retry available after error |
| BR-03 | error-handling.md | Tests #3–5 — messages in Portuguese |
| BR-04 | error-handling.md | Tests #3–5 — `toUserMessage()` used for infrastructure errors |

## Setup

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: ExerciseRepository = mockk()
    private val useCase = GetBodyPartsUseCase(repository)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(useCase)
    }
}
```

## Files to create

- `domain/usecase/GetBodyPartsUseCaseTest.kt`
- `presentation/home/HomeViewModelTest.kt`
- `util/MainDispatcherRule.kt` — test helper for `Dispatchers.Main`
