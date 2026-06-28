# Spec: Bug Fix — GIF Exibindo como Imagem Estática

**ID:** BUG-002  
**Severity:** 🔴 Critical  
**Found in:** Teste regressivo pós FEAT-003  
**Affects:** ExerciseListScreen, ExerciseDetailScreen  
**Status:** Approved

## Description

Os GIFs dos exercícios estão sendo exibidos como imagens estáticas — a animação
não reproduz. O endpoint `/image` retorna um GIF animado (`Content-Type: image/gif`)
mas o Coil não está configurado para decodificar e reproduzir animações GIF.

## Root Cause

O Coil requer um `ImageLoader` customizado com `GifDecoder` para reproduzir GIFs.
Sem essa configuração, o Coil renderiza apenas o primeiro frame do GIF como imagem estática.

A dependência `coil-gif` já está no projeto (adicionada em CORE-000), mas o
`ImageLoader` provavelmente não foi configurado na `Application` class.

## Expected Behavior

- GIFs animados reproduzem continuamente nas telas de lista e detalhe
- A animação começa automaticamente ao carregar
- Placeholder é exibido enquanto o GIF carrega

## Fix

**1. Configurar ImageLoader na Application class:**

```kotlin
// GymLibraryApplication.kt
class GymLibraryApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(AnimatedImageDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
}
```

**2. Verificar AsyncImage nas telas:**

```kotlin
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(gifUrl)
        .crossfade(true)
        .build(),
    contentDescription = exercise.name,
    placeholder = ColorPainter(MaterialTheme.colorScheme.surface)
)
```

## Business Rules

- BR-01: GIFs devem ser animados — não frames estáticos
- BR-02: `ImageLoaderFactory` deve ser implementado em `GymLibraryApplication`
- BR-03: `AnimatedImageDecoder` para API 28+ e `GifDecoder` para API 26–27 (minSdk = 26)
- BR-04: Placeholder deve aparecer enquanto o GIF carrega

## Files to modify

- `GymLibraryApplication.kt` — implementar `ImageLoaderFactory`
- `presentation/exerciselist/ExerciseListScreen.kt` — verificar `ImageRequest`
- `presentation/exercisedetail/ExerciseDetailScreen.kt` — verificar `ImageRequest`

## Acceptance Criteria

- [ ] GIFs animam continuamente na ExerciseListScreen
- [ ] GIFs animam continuamente na ExerciseDetailScreen
- [ ] Placeholder visível enquanto GIF carrega
- [ ] Sem crash em dispositivos API 26 e 27
- [ ] Sem crash em dispositivos API 28+
