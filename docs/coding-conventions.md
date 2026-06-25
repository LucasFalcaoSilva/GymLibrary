# Coding Conventions — GymLibrary

Follows the same conventions established in GameShelf.

## Naming

| Artifact | Convention | Example |
|---|---|---|
| ViewModel state | `data class XxxUiState` | `ExerciseListUiState` |
| Screen composable | `XxxScreen` | `ExerciseDetailScreen` |
| Use case | `XxxUseCase` | `GetExercisesUseCase` |
| Repository interface | `XxxRepository` | `ExerciseRepository` |
| Repository impl | `XxxRepositoryImpl` | `ExerciseRepositoryImpl` |
| DTO | `XxxDto` | `ExerciseDto` |
| Mapper function | `fun XxxDto.toDomain()` | `ExerciseDto.toDomain()` |
| Koin module | `val xxxModule = module {}` | `val networkModule` |
| Route object | `object XxxRoute` | `object ExerciseDetailRoute` |

## ViewModel

- Expose a single `StateFlow<UiState<T>>` per screen
- Use `viewModelScope` + `Dispatchers.IO` for API calls
- No Android framework references in Domain or Data layers

## Coroutines

- `viewModelScope.launch` in ViewModel
- `withContext(Dispatchers.IO)` in Repository
- Suspend functions in UseCase and DataSource

## Compose

- One composable file per screen
- Extract reusable components to `core/ui/components/`
- Use `LazyColumn` for lists
- Use `AsyncImage` from Coil for GIF loading

## Commits

Follow Conventional Commits:

```
feat: add exercise list screen
fix: handle empty body part list
docs: update architecture diagram
chore: add coil-gif dependency
refactor: extract muscle chip composable
test: add GetExercisesUseCase unit test
```

## Error Handling

- Network errors wrapped in `Result<T>` in Repository
- ViewModel maps `Result` to `UiState.Error`
- UI shows error message + retry button on every screen
