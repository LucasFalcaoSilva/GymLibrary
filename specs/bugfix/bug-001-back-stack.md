# Spec: Bug Fix — Back Stack Corrompido

**ID:** BUG-001  
**Severity:** 🔴 Critical  
**Found in:** Teste regressivo pós FEAT-003  
**Affects:** Navegação global — todas as telas  
**Status:** Approved

## Description

Ao pressionar o botão voltar duas vezes consecutivas, o fluxo de navegação se perde.
O comportamento esperado é:

```
ExerciseDetailScreen → voltar → ExerciseListScreen → voltar → HomeScreen
```

O comportamento atual resulta em tela em branco, app fechando inesperadamente,
ou retorno para uma tela incorreta.

## Root Cause (hypothesis)

O `NavGraph` provavelmente não está configurando `popUpTo` e `launchSingleTop`
corretamente nas rotas, causando acúmulo de instâncias no back stack.

Verificar em `presentation/navigation/NavGraph.kt`:
- Se `popUpTo(HomeRoute.route) { inclusive = false }` está ausente nas navegações
- Se telas estão sendo empilhadas múltiplas vezes ao navegar

## Expected Behavior

| Ação | Tela atual | Resultado esperado |
|---|---|---|
| Toque em body part card | HomeScreen | Navega para ExerciseListScreen |
| Toque em exercise card | ExerciseListScreen | Navega para ExerciseDetailScreen |
| Botão voltar | ExerciseDetailScreen | Retorna para ExerciseListScreen |
| Botão voltar | ExerciseListScreen | Retorna para HomeScreen |
| Botão voltar | HomeScreen | Fecha o app |

## Business Rules

- BR-01: O back stack deve ter no máximo 3 entradas simultâneas (Home + List + Detail)
- BR-02: HomeScreen nunca deve ser instanciada mais de uma vez no back stack
- BR-03: Pressionar voltar na HomeScreen deve fechar o app, não navegar para tela em branco
- BR-04: `launchSingleTop = true` deve ser configurado em todas as navegações

## Fix Guidance

```kotlin
// NavGraph.kt — navegação correta
navController.navigate(ExerciseListRoute.createRoute(bodyPart)) {
    launchSingleTop = true
}

navController.navigate(ExerciseDetailRoute.createRoute(exerciseId)) {
    launchSingleTop = true
}
```

## Files to modify

- `presentation/navigation/NavGraph.kt`

## Acceptance Criteria

- [ ] Home → List → Detail → voltar → List → voltar → Home funciona corretamente
- [ ] Pressionar voltar múltiplas vezes não corrompe o back stack
- [ ] HomeScreen não é duplicada no back stack
- [ ] Pressionar voltar na HomeScreen fecha o app
