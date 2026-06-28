# Spec: Improvement — App Icon + Home Title

**ID:** IMP-002  
**Priority:** 🟡 Medium  
**Type:** Visual / Branding  
**Status:** Approved

## Description

Duas melhorias visuais independentes agrupadas por simplicidade de implementação:

1. Substituir o ícone padrão do Android por um ícone temático do GymLibrary
2. Centralizar o título na top app bar da HomeScreen

---

## IMP-002A — App Icon

### Current State
Ícone padrão do Android (robozinho verde).

### Expected
Ícone temático de academia — haltere ou figura de exercício — nas cores do app
(vermelho `#E53935` sobre fundo escuro `#121212`).

### Implementation

Usar **Adaptive Icon** (API 26+) — obrigatório para minSdk 26:

```
res/
├── drawable/
│   ├── ic_launcher_foreground.xml   ← haltere SVG (vermelho)
│   └── ic_launcher_background.xml  ← fundo escuro
└── mipmap-anydpi-v26/
    └── ic_launcher.xml              ← adaptive icon
```

**Foreground — haltere SVG simplificado:**
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <!-- haltere centralizado com cor primary #E53935 -->
</vector>
```

### Business Rules

- BR-01: Ícone deve usar Adaptive Icon (`mipmap-anydpi-v26`)
- BR-02: Foreground: haltere ou figura de exercício na cor `primary` (#E53935)
- BR-03: Background: cor `background` (#121212)
- BR-04: Ícone deve ser legível em tamanhos 48dp, 72dp e 96dp

### Files to create/modify

- `res/drawable/ic_launcher_foreground.xml`
- `res/drawable/ic_launcher_background.xml`
- `res/mipmap-anydpi-v26/ic_launcher.xml`
- `res/mipmap-anydpi-v26/ic_launcher_round.xml`
- `AndroidManifest.xml` — verificar referência ao ícone

---

## IMP-002B — Home Title Centralizado

### Current State
Título da HomeScreen alinhado à esquerda (padrão Material3 `TopAppBar`).

### Expected
Título centralizado na top app bar da HomeScreen.

### Implementation

Usar `CenterAlignedTopAppBar` em vez de `TopAppBar`:

```kotlin
// HomeScreen.kt
CenterAlignedTopAppBar(
    title = {
        Text(
            text = "GymLibrary",
            style = MaterialTheme.typography.titleLarge
        )
    }
)
```

### Business Rules

- BR-01: Usar `CenterAlignedTopAppBar` apenas na HomeScreen
- BR-02: ExerciseListScreen e ExerciseDetailScreen mantêm `TopAppBar` padrão (com botão voltar à esquerda)

### Files to modify

- `presentation/home/HomeScreen.kt`

---

## Acceptance Criteria

**IMP-002A:**
- [ ] Ícone temático aparece na launcher
- [ ] Ícone round funciona corretamente
- [ ] Ícone legível em fundo claro e escuro no launcher

**IMP-002B:**
- [ ] Título "GymLibrary" centralizado na HomeScreen
- [ ] ExerciseListScreen e ExerciseDetailScreen não afetadas
