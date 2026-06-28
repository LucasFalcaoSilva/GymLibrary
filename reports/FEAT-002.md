# Implementation Report — FEAT-002

**Date:** 2026-06-28  
**Spec:** specs/feature/exercise-list-screen.md  
**Commit:** pending  
**Build:** ✅ BUILD SUCCESSFUL  

---

## Spec Summary

Implements the Exercise List Screen that displays a paginated vertical list of exercises filtered by the selected body part. Each card shows a 80dp animated GIF, the capitalized exercise name, and the target muscle. Pagination loads 20 exercises at a time; reaching the bottom triggers `loadMore()`. Initial-load errors show `ErrorState` with retry; pagination errors show a Snackbar. Tapping a card navigates to `ExerciseDetailScreen`.

## Files Created

| File | Description |
|------|-------------|
| `domain/model/Exercise.kt` | Domain model with all fields from spec |
| `data/remote/dto/ExerciseDto.kt` | `@Serializable` DTO matching API response |
| `data/mapper/ExerciseMapper.kt` | `ExerciseDto.toDomain()` extension function |
| `domain/usecase/GetExercisesUseCase.kt` | Delegates to `ExerciseRepository.getExercisesByBodyPart()` |
| `presentation/exerciselist/ExerciseListViewModel.kt` | `StateFlow<ExerciseListUiState>`, pagination state machine, `dismissError()` |
| `presentation/exerciselist/ExerciseListScreen.kt` | `ExerciseListScreen`, `ExerciseCard`, `LoadingMoreIndicator` composables |

## Files Modified

| File | Change |
|------|--------|
| `data/remote/api/ExerciseDbService.kt` | Added `getExercisesByBodyPart()` endpoint |
| `data/remote/datasource/ExerciseRemoteDataSource.kt` | Added `getExercisesByBodyPart()` with `runCatching` |
| `data/repository/ExerciseRepositoryImpl.kt` | Implemented `getExercisesByBodyPart()` with DTO→domain mapping |
| `domain/repository/ExerciseRepository.kt` | Added `getExercisesByBodyPart()` to interface |
| `core/di/DomainModule.kt` | Added `factory { GetExercisesUseCase(get()) }` |
| `core/di/PresentationModule.kt` | Added parameterized `viewModel { params -> ExerciseListViewModel(get(), params.get()) }` |
| `presentation/navigation/NavGraph.kt` | Wired `ExerciseListScreen` into the ExerciseList route placeholder |

## Business Rules Compliance

| BR | Source | Description | Status |
|----|--------|-------------|--------|
| BR-01 | exercise-list-screen.md | Pagination limit=20 | ✅ |
| BR-02 | exercise-list-screen.md | Exercise name capitalized | ✅ |
| BR-03 | exercise-list-screen.md | Top bar with bodyPart + back button | ✅ |
| BR-04 | exercise-list-screen.md | GIF via AsyncImage with placeholder | ✅ |
| BR-04 | error-handling.md | ViewModels use `toUserMessage()` | ✅ |
| BR-05 | error-handling.md | `runCatching` in RemoteDataSource | ✅ |
| BR-06 | error-handling.md | `ErrorState` composable on every screen | ✅ |

## Deferred to Next Spec

| Item | Target Spec |
|------|-------------|
| `ExerciseDetailScreen` composable wired into NavGraph ExerciseDetail route | FEAT-003 |

---

## Review Round 1

**Date:** 2026-06-28  
**Build:** ✅ BUILD SUCCESSFUL  
**Triggered by:** /implement flow  

### BR Compliance

| BR | Source | Description | Status |
|----|--------|-------------|--------|
| BR-01 | exercise-list-screen.md | Pagination limit=20 | ✅ |
| BR-02 | exercise-list-screen.md | Exercise name capitalized | ✅ |
| BR-03 | exercise-list-screen.md | Top bar with bodyPart + back button | ✅ |
| BR-04 | exercise-list-screen.md | GIF via AsyncImage with placeholder | ✅ |
| BR-04 | error-handling.md | ViewModels use `toUserMessage()` | ✅ |
| BR-05 | error-handling.md | `runCatching` in RemoteDataSource | ✅ |
| BR-06 | error-handling.md | `ErrorState` composable on every screen | ✅ |

### Findings

| # | Severity | File:Line | Description | Status |
|---|----------|-----------|-------------|--------|
| 1 | 🟡 | ExerciseListScreen.kt:54 | AF-03 snackbar shows error-mapper message instead of spec-stated fixed string "Erro ao carregar mais exercícios. Tente novamente." — messages are more informative and still in PT-BR | Deferred — no BR violated |

### Out of Scope

| File:Line | Description | Target Spec |
|-----------|-------------|-------------|
| NavGraph.kt | ExerciseDetail route has no composable call yet | FEAT-003 |

---

## Review Round 2

**Date:** 2026-06-28  
**Build:** ✅ BUILD SUCCESSFUL  
**Triggered by:** /implement FEAT-002 — spec e error-handling.md atualizados

### What changed

- Spec AF-03 atualizado: exige string fixa para snackbar de paginação, proíbe `toUserMessage()`
- `error-handling.md` reestruturado: distingue Infrastructure Errors (→ `toUserMessage()`) de Operation Errors (→ string fixa da spec)
- Fix aplicado: `ExerciseListViewModel.loadMore()` — `e.toUserMessage()` substituído pela string fixa `"Erro ao carregar mais exercícios. Tente novamente."` (BR-04 EH, BR-05 EH)

### BR Compliance

| BR | Source | Description | Status |
|----|--------|-------------|--------|
| BR-01 | exercise-list-screen.md | Pagination limit=20 | ✅ |
| BR-02 | exercise-list-screen.md | Exercise name capitalized | ✅ |
| BR-03 | exercise-list-screen.md | Top bar with bodyPart + back button | ✅ |
| BR-04 | exercise-list-screen.md | GIF via AsyncImage with placeholder | ✅ |
| BR-01 | error-handling.md | No technical errors in UI | ✅ |
| BR-02 | error-handling.md | Full-screen error has retry | ✅ |
| BR-03 | error-handling.md | All messages in PT-BR | ✅ |
| BR-04 | error-handling.md | `toUserMessage()` only for infrastructure errors | ✅ |
| BR-05 | error-handling.md | Operation errors use fixed spec strings | ✅ |
| BR-06 | error-handling.md | `runCatching` in RemoteDataSource | ✅ |
| BR-07 | error-handling.md | `ErrorState` for full-screen errors | ✅ |

### Findings

No findings — all BRs passed. Round 1 🟡 finding resolved by spec update (now BR-05 EH, fixed before commit).

### Out of Scope

| File:Line | Description | Target Spec |
|-----------|-------------|-------------|
| NavGraph.kt | ExerciseDetail route has no composable call yet | FEAT-003 |

---

## Review Round 3

**Date:** 2026-06-28  
**Build:** ✅ BUILD SUCCESSFUL  
**Triggered by:** /implement FEAT-002 — spec atualizada (remoção de `isLoadingMore`)

### What changed

- Spec removeu `isLoadingMore: Boolean` de `ExerciseListUiState`
- `ExerciseListViewModel` reescrito: guard de concorrência movido para `loadMoreJob: Job?` privado
- `loadExercises()` failure manteve `toUserMessage()` (infra error — corrigido após modificação incorreta por linter)
- `ExerciseListScreen` atualizado: `LoadingMoreIndicator` mostrado quando `!state.hasReachedEnd`

### BR Compliance

| BR | Source | Description | Status |
|----|--------|-------------|--------|
| BR-01 | exercise-list-screen.md | Pagination limit=20 | ✅ |
| BR-02 | exercise-list-screen.md | Exercise name capitalized | ✅ |
| BR-03 | exercise-list-screen.md | Top bar with bodyPart + back button | ✅ |
| BR-04 | exercise-list-screen.md | GIF via AsyncImage with placeholder | ✅ |
| BR-04 | error-handling.md | `toUserMessage()` only for infrastructure errors | ✅ |
| BR-05 | error-handling.md | Operation errors use fixed spec strings | ✅ |
| BR-06 | error-handling.md | `runCatching` in RemoteDataSource | ✅ |
| BR-07 | error-handling.md | `ErrorState` for full-screen errors | ✅ |

### Findings

No findings — all BRs passed.

### Out of Scope

| File:Line | Description | Target Spec |
|-----------|-------------|-------------|
| NavGraph.kt | ExerciseDetail route has no composable call yet | FEAT-003 |

---

## Review Round 4

**Date:** 2026-06-28  
**Build:** ✅ BUILD SUCCESSFUL  
**Triggered by:** /review FEAT-002 — spec atualizada (modelo Exercise, gifUrl client-side, isLoadingMore restored)

### What changed

- `Exercise` domain model: remove `gifUrl`, adiciona `description`/`difficulty`/`category`
- `ExerciseDto`: mantém `gifUrl: String = ""` (intencional, user/linter); adiciona novos campos
- `ExerciseMapper`: remove mapeamento de `gifUrl`, mapeia novos campos
- `AuthInterceptor`: adiciona `gifUrl(exerciseId)` companion function — URL construída com key embutida
- `ExerciseListScreen`: `exercise.gifUrl` → `AuthInterceptor.gifUrl(exercise.id)`; `LoadingMoreIndicator` condicionado a `state.isLoadingMore`
- `ExerciseListUiState`: `isLoadingMore: Boolean = false` restaurado ao estado

### BR Compliance

| BR | Source | Description | Status |
|----|--------|-------------|--------|
| BR-01 | exercise-list-screen.md | Pagination limit=20 | ✅ |
| BR-02 | exercise-list-screen.md | Exercise name capitalized | ✅ |
| BR-03 | exercise-list-screen.md | Top bar with bodyPart + back button | ✅ |
| BR-04 | exercise-list-screen.md | GIF via AsyncImage, URL construída com exerciseId + API key | ✅ |
| BR-04 | error-handling.md | `toUserMessage()` only for infrastructure errors | ✅ |
| BR-05 | error-handling.md | Operation errors use fixed spec strings | ✅ |
| BR-06 | error-handling.md | `runCatching` in RemoteDataSource | ✅ |
| BR-07 | error-handling.md | `ErrorState` for full-screen errors | ✅ |

### Findings

| # | Severity | File:Line | Description | Status |
|---|----------|-----------|-------------|--------|
| 1 | 🟡 | ExerciseDto.kt:9 | `gifUrl: String = ""` não está no DTO da spec — adicionado intencionalmente para safe deserialization; mapper não usa o campo | Aceito — sem impacto funcional |

### Out of Scope

| File:Line | Description | Target Spec |
|-----------|-------------|-------------|
| NavGraph.kt | ExerciseDetail route has no composable call yet | FEAT-003 |
| Exercise.kt | Campos `description`, `difficulty`, `category` não exibidos em nenhuma tela ainda | FEAT-003 |

---

## QA Sign-off

**Status:** ✅ Approved  
**Date:** 2026-06-28  
**Device/Emulator:** Medium Phone  
**Android version:** 16  
**Tested by:** Lucas  

| TC | Description | Result |
|----|-------------|--------|
| TC-001 | Happy Path: Exercise list loads successfully | ✅ Pass |
| TC-002 | Pagination: Load more exercises on scroll | ✅ Pass |
| TC-003 | Navigation: Tap an exercise card | ✅ Pass |
| TC-004 | Error State: No network on initial load | ✅ Pass |
| TC-005 | Retry: Recover from initial load error | ✅ Pass |
| TC-006 | Pagination Error: Snackbar on load-more failure | ✅ Pass |
| TC-007 | Back Navigation | ✅ Pass |
| TC-008 | Visual: Theme and layout | ✅ Pass |

**Failed TCs:** None  
**Notes:** —
