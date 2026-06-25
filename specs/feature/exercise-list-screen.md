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

**AF-02 — Lista vazia:**  
Exibe mensagem "Nenhum exercício encontrado para este grupo muscular".

**AF-03 — Erro ao carregar mais itens (paginação):**  
Exibe snackbar "Erro ao carregar mais exercícios. Tente novamente."

## Business Rules

- BR-01: Paginação com `limit=20` por página
- BR-02: Nome do exercício exibido capitalizado
- BR-03: Top app bar exibe o nome do `bodyPart` capitalizado + botão voltar
- BR-04: GIF deve ser carregado com Coil (`AsyncImage`) com placeholder

## Domain

**UseCase:** `GetExercisesUseCase`  
**Input:** `bodyPart: String, limit: Int, offset: Int`  
**Repository:** `ExerciseRepository.getExercisesByBodyPart(bodyPart, limit, offset): Result<List<Exercise>>`

**Model:**
```kotlin
data class Exercise(
    val id: String,
    val name: String,
    val gifUrl: String,
    val target: String,
    val bodyPart: String,
    val equipment: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)
```

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

## DTO

```kotlin
@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val gifUrl: String,
    val target: String,
    val bodyPart: String,
    val equipment: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)
```

## Files to create

- `data/remote/dto/ExerciseDto.kt`
- `data/mapper/ExerciseMapper.kt`
- `domain/model/Exercise.kt`
- `domain/usecase/GetExercisesUseCase.kt`
- `domain/repository/ExerciseRepository.kt`
- `data/remote/api/ExerciseDbService.kt`
- `data/remote/datasource/ExerciseRemoteDataSource.kt`
- `data/repository/ExerciseRepositoryImpl.kt`
- `presentation/exerciselist/ExerciseListViewModel.kt`
- `presentation/exerciselist/ExerciseListScreen.kt`
