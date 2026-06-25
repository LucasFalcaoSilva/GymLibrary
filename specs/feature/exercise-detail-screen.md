# Spec: Exibir Detalhe do Exercício

**ID:** FEAT-003  
**Feature:** Exercise Detail  
**Status:** Approved

## Description

O usuário vê o detalhe completo de um exercício: GIF animado em tamanho grande, músculos ativados (primário e secundários), equipamento necessário e instruções passo a passo.

## Preconditions

- `exerciseId` recebido via argumento de navegação
- Rede disponível

## Main Flow

1. App exibe `LoadingState` enquanto busca o detalhe
2. Sistema chama `GET /exercises/exercise/{id}`
3. API retorna objeto de exercício completo
4. App exibe tela de detalhe com:
   - GIF animado em largura total (aspect ratio 16:9)
   - Nome do exercício (headline)
   - Chips: `bodyPart`, `equipment`
   - Seção "Músculos Ativados": músculo primário + lista de secundários
   - Seção "Instruções": lista numerada passo a passo
5. Top app bar exibe nome do exercício + botão voltar

## Alternative Flows

**AF-01 — Erro de rede:**  
Exibe mensagem de erro + botão "Tentar novamente" que re-executa o passo 2.

## Business Rules

- BR-01: Nome do exercício deve ser exibido capitalizado
- BR-02: Chips de `bodyPart` e `equipment` devem usar `AssistChip` do Material3
- BR-03: Músculos secundários exibidos como lista separada por vírgula
- BR-04: Instruções numeradas começando em 1
- BR-05: GIF deve ter placeholder enquanto carrega (cor `surface`)

## Domain

**UseCase:** `GetExerciseDetailUseCase`  
**Input:** `exerciseId: String`  
**Repository:** `ExerciseRepository.getExerciseById(id): Result<Exercise>`  
**Model:** reutiliza `Exercise` definido em FEAT-002

## Presentation

**ViewModel:** `ExerciseDetailViewModel`  
**State:**
```kotlin
data class ExerciseDetailUiState(
    val uiState: UiState<Exercise> = UiState.Loading
)
```

**Screen:** `ExerciseDetailScreen`  
**Components:**
- `ExerciseGif` — `AsyncImage` largura total, aspect ratio 1f
- `MuscleChipRow` — Row com `AssistChip` para bodyPart e equipment
- `MuscleSection` — músculo primário destacado + lista de secundários
- `InstructionList` — `LazyColumn` de passos numerados

## Layout Structure

```
Column (scrollable)
  ├── ExerciseGif (full width)
  ├── Text(name, headline)
  ├── MuscleChipRow (bodyPart, equipment)
  ├── Divider
  ├── MuscleSection
  │     ├── Text("Músculos Ativados", title)
  │     ├── Text("Primário: ${target}", body)
  │     └── Text("Secundários: ${secondaryMuscles.joinToString()}", body)
  ├── Divider
  └── InstructionList
        ├── Text("Instruções", title)
        └── [1. step1, 2. step2, ...]
```

## Files to create

- `domain/usecase/GetExerciseDetailUseCase.kt`
- `presentation/exercisedetail/ExerciseDetailViewModel.kt`
- `presentation/exercisedetail/ExerciseDetailScreen.kt`
