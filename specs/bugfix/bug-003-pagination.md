# Spec: Bug Fix — Paginação Não Carregando Mais Exercícios

**ID:** BUG-003  
**Severity:** 🔴 Critical  
**Found in:** Teste regressivo pós FEAT-003  
**Affects:** ExerciseListScreen  
**Status:** Approved

## Description

Ao rolar até o final da lista de exercícios, mais exercícios não são carregados.
A paginação não está sendo disparada corretamente.

## Root Cause (hypothesis)

O detector de fim de lista no `LazyColumn` provavelmente não está detectando
quando o último item fica visível. Causas comuns:

1. O índice do último item não está sendo monitorado corretamente via `LazyListState`
2. O threshold de detecção está incorreto (detecta apenas quando o item já passou)
3. `loadMore()` está sendo chamado mas o ViewModel não está processando corretamente
4. `hasReachedEnd` sendo setado prematuramente

## Expected Behavior

1. Usuário chega próximo ao fim da lista (últimos 3-5 itens visíveis)
2. `loadMore()` é disparado automaticamente
3. `LoadingMoreIndicator` aparece no final da lista
4. Mais 20 exercícios são adicionados à lista
5. Total de exercícios aumenta progressivamente

## Fix Guidance

**Detector de fim de lista correto:**

```kotlin
// ExerciseListScreen.kt
val listState = rememberLazyListState()

// Detecta quando chegou perto do fim
val shouldLoadMore = remember {
    derivedStateOf {
        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            ?: return@derivedStateOf false
        val totalItems = listState.layoutInfo.totalItemsCount
        lastVisibleItem.index >= totalItems - 5  // threshold: 5 itens antes do fim
    }
}

LaunchedEffect(shouldLoadMore.value) {
    if (shouldLoadMore.value) {
        viewModel.loadMore()
    }
}
```

**ViewModel — guard contra chamadas duplicadas:**

```kotlin
// ExerciseListViewModel.kt
fun loadMore() {
    val state = _uiState.value
    if (state.isLoadingMore || state.hasReachedEnd) return

    viewModelScope.launch {
        _uiState.update { it.copy(isLoadingMore = true) }
        // ...
    }
}
```

## Business Rules

- BR-01: `loadMore()` deve ser disparado quando os últimos 5 itens da lista ficam visíveis
- BR-02: `loadMore()` não deve ser chamado se `isLoadingMore = true` ou `hasReachedEnd = true`
- BR-03: `LoadingMoreIndicator` deve ser visível enquanto `isLoadingMore = true`
- BR-04: `hasReachedEnd = true` quando a API retornar menos de `limit` itens

## Files to modify

- `presentation/exerciselist/ExerciseListScreen.kt` — corrigir detector de fim de lista
- `presentation/exerciselist/ExerciseListViewModel.kt` — verificar guard e `hasReachedEnd`

## Acceptance Criteria

- [ ] Ao rolar até o fim, mais exercícios são carregados automaticamente
- [ ] `LoadingMoreIndicator` aparece enquanto carrega
- [ ] `loadMore()` não é chamado múltiplas vezes simultaneamente
- [ ] Quando API retorna menos de 20 itens, paginação para
- [ ] Lista não duplica exercícios existentes
