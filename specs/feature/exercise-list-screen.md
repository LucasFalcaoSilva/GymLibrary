# Spec: Listar Exercícios por Grupo Muscular

**ID:** FEAT-002  
**Feature:** Exercise List  
**Status:** Approved

## Description

O usuário vê a lista de exercícios filtrados pelo grupo muscular selecionado na Home. Pode rolar a lista e tocar em um exercício para ver detalhes.

## Preconditions

- `bodyPart` recebido via argumento de navegação
- Rede disponível

## Main Flow

1. App exibe `LoadingState` enquanto busca os exercícios
2. Sistema chama `GET /exercises/bodyPart/{bodyPart}` com `limit=20&offset=0`
3. API retorna lista de exercícios
4. App exibe lista vertical com um card por exercício
5. Cada card exibe: GIF animado + nome do exercício + músculo alvo
6. Usuário rola até o final da lista → app carrega mais 20 exercícios (paginação)
7. Usuário toca em um card → navega para `ExerciseDetailScreen` com `exerciseId`

## Alternative Flows

**AF-01 — Erro de rede:**  
Exibe mensagem de erro + botão "Tentar novamente".  
Ver `docs/error-handling.md` para mapeamento de erros e componente `ErrorState`.

**AF-02 — Lista vazia:**  
Exibe mensagem "Nenhum exercício encontrado para este grupo muscular".

**AF-03 — Erro ao carregar mais itens (paginação):**  
Exibe Snackbar com a mensagem fixa: `"Erro ao carregar mais exercícios. Tente novamente."`  
Não usar `toUserMessage()` — este é um erro de operação, não de infraestrutura. Ver `docs/error-handling.md`.

## Business Rules

- BR-01: Paginação com `limit=20` por página
- BR-02: Nome do exercício exibido capitalizado
- BR-03: Top app bar exibe o nome do `bodyPart` capitalizado + botão voltar
- BR-04: GIF carregado via Coil `AsyncImage` usando URL construída com `exerciseId` — endpoint `GET /image?exerciseId={id}&resolution=360&rapidapi-key={key}`. O header `X-RapidAPI-Key` deve ser incluído na request via OkHttp interceptor ou ImageRequest customizado.

## Domain

**UseCase:** `GetExercisesUseCase`  
**Input:** `bodyPart: String, limit: Int, offset: Int`  
**Repository:** `ExerciseRepository.getExercisesByBodyPart(bodyPart, limit, offset): Result<List<Exercise>>`  
**Note:** `ExerciseRepository` interface and `ExerciseRepositoryImpl` already exist from FEAT-001 — add the new method only.

**Model:**
```kotlin
data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String,
    val difficulty: String,
    val category: String
)
```

> `gifUrl` is not part of the domain model — it is constructed client-side in the UI layer
> using `AuthInterceptor.gifUrl(exerciseId)` or equivalent helper.

## Data

**DTO:**
```kotlin
@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String,
    val difficulty: String,
    val category: String
)
```

**GIF URL:** Not returned by the exercises endpoint. Construct it client-side using the exercise `id`:
```kotlin
fun gifUrl(exerciseId: String, apiKey: String) =
    "https://exercisedb.p.rapidapi.com/image?exerciseId=$exerciseId&resolution=360&rapidapi-key=$apiKey"
```

> The `/image` endpoint streams a GIF directly (`Content-Type: image/gif`) — it is not JSON.
> Load it via Coil `AsyncImage` using the constructed URL with the `X-RapidAPI-Key` header.

**API Service:** Add to existing `ExerciseDbService`:
```kotlin
@GET("exercises/bodyPart/{bodyPart}")
suspend fun getExercisesByBodyPart(
    @Path("bodyPart") bodyPart: String,
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
): List<ExerciseDto>
```

**DataSource:** Add `getExercisesByBodyPart()` to existing `ExerciseRemoteDataSource`  
**Repository:** Add `getExercisesByBodyPart()` to existing `ExerciseRepositoryImpl`

## Presentation

**ViewModel:** `ExerciseListViewModel`  
**State:**
```kotlin
data class ExerciseListUiState(
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasReachedEnd: Boolean = false
)
```

**Screen:** `ExerciseListScreen`  
**Components:**
- `ExerciseCard` — Row com GIF (80dp) + nome + músculo alvo
- `LoadingMoreIndicator` — CircularProgressIndicator no fim da lista

## Files to create

- `data/remote/dto/ExerciseDto.kt`
- `data/mapper/ExerciseMapper.kt`
- `domain/model/Exercise.kt`
- `domain/usecase/GetExercisesUseCase.kt`
- `presentation/exerciselist/ExerciseListViewModel.kt`
- `presentation/exerciselist/ExerciseListScreen.kt`

## Files to modify

- `data/remote/api/ExerciseDbService.kt` — add `getExercisesByBodyPart()` endpoint
- `data/remote/datasource/ExerciseRemoteDataSource.kt` — add `getExercisesByBodyPart()`
- `data/repository/ExerciseRepositoryImpl.kt` — implement `getExercisesByBodyPart()`
- `domain/repository/ExerciseRepository.kt` — add `getExercisesByBodyPart()` to interface
- `core/di/DomainModule.kt` — add `GetExercisesUseCase` binding
- `core/di/PresentationModule.kt` — add `ExerciseListViewModel` binding