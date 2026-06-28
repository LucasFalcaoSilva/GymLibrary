# Spec: Exibir Grupos Musculares (Home)

**ID:** FEAT-001  
**Feature:** Home  
**Status:** Approved

## Description

O usuário abre o app e vê uma grade de grupos musculares. Ao tocar em um grupo, navega para a lista de exercícios filtrada.

## Preconditions

- Rede disponível
- API key configurada

## Main Flow

1. App exibe `LoadingState` enquanto busca a lista de grupos musculares
2. Sistema chama `GET /exercises/bodyPartList`
3. API retorna lista de strings (ex: `["back", "chest", "legs", ...]`)
4. App exibe grade 2 colunas com um card por grupo muscular
5. Cada card exibe: nome do grupo capitalizado + ícone representativo
6. Usuário toca em um card → navega para `ExerciseListScreen` com o `bodyPart` selecionado

## Alternative Flows

**AF-01 — Erro de rede:**  
Sistema exibe mensagem de erro + botão "Tentar novamente" que re-executa o passo 2.  
Ver `docs/error-handling.md` para mapeamento de erros e componente `ErrorState`.

**AF-02 — Lista vazia:**  
Não esperado da API, mas se ocorrer exibe mensagem "Nenhum grupo encontrado".

## Business Rules

- BR-01: Nomes de grupos musculares devem ser capitalizados na UI (`"back"` → `"Back"`)
- BR-02: A ordem exibida deve seguir a ordem retornada pela API

## Domain

**UseCase:** `GetBodyPartsUseCase`  
**Repository:** `ExerciseRepository.getBodyParts(): Result<List<String>>`

## Data

**DTO:**
```kotlin
// Nenhum DTO necessário — endpoint retorna List<String> diretamente
```

**API Service:**
```kotlin
interface ExerciseDbService {
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): List<String>
}
```

**DataSource:** `ExerciseRemoteDataSource.getBodyParts(): Result<List<String>>`  
**Repository:** `ExerciseRepositoryImpl` implementa `ExerciseRepository`

## Presentation

**ViewModel:** `HomeViewModel`  
**State:**
```kotlin
data class HomeUiState(
    val uiState: UiState<List<String>> = UiState.Loading
)
```

**Screen:** `HomeScreen`  
**Components:**
- `BodyPartGrid` — LazyVerticalGrid 2 colunas
- `BodyPartCard` — Card com nome + ícone

## Icons per BodyPart (suggestion)

| BodyPart | Icon |
|---|---|
| back | `Icons.Default.FitnessCenter` |
| chest | `Icons.Default.FavoriteBorder` |
| upper arms | `Icons.Default.SportsMma` |
| lower arms | `Icons.Default.PanTool` |
| shoulders | `Icons.Default.AccessibilityNew` |
| upper legs | `Icons.AutoMirrored.Filled.DirectionsWalk` |
| lower legs | `Icons.AutoMirrored.Filled.DirectionsRun` |
| waist | `Icons.Default.RadioButtonUnchecked` |
| cardio | `Icons.Default.MonitorHeart` |
| neck | `Icons.Default.Person` |

## Files to create

- `data/remote/api/ExerciseDbService.kt`
- `data/remote/datasource/ExerciseRemoteDataSource.kt`
- `data/repository/ExerciseRepositoryImpl.kt`
- `domain/repository/ExerciseRepository.kt`
- `domain/usecase/GetBodyPartsUseCase.kt`
- `presentation/home/HomeViewModel.kt`
- `presentation/home/HomeScreen.kt`