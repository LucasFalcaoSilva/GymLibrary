# Spec: Theme Setup

**ID:** CORE-004  
**Layer:** Core / UI  
**Status:** Approved

## Description

Configure Material3 theme for the app with a dark, gym-inspired visual identity.

## Behavior

1. App uses `MaterialTheme` with a custom `ColorScheme`
2. Default theme is **dark** — gym apps have dark backgrounds as standard
3. No dynamic color (Material You) — ensures visual consistency across devices
4. Typography uses default Material3 type scale (no custom fonts in v1)

## Color Palette

| Token | Value | Usage |
|---|---|---|
| `primary` | `#E53935` (red) | CTAs, active state |
| `onPrimary` | `#FFFFFF` | Text on primary |
| `background` | `#121212` | Screen background |
| `surface` | `#1E1E1E` | Cards, bottom sheets |
| `onBackground` | `#FFFFFF` | Primary text |
| `onSurface` | `#B0B0B0` | Secondary text |

## Files to create

- `core/ui/theme/Color.kt` — color constants
- `core/ui/theme/Theme.kt` — `GymLibraryTheme` composable wrapping `MaterialTheme`
- `core/ui/theme/Type.kt` — `AppTypography` definition (named `AppTypography`, not `Typography`, to avoid shadowing the M3 class)
- `res/values/themes.xml` — defines `Theme.GymLibrary` required by `AndroidManifest.xml`

## themes.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.GymLibrary" parent="Theme.AppCompat.Light.NoActionBar" />
</resources>
```

## Additional dependency required

Add to `app/build.gradle.kts` when implementing this spec:

```kotlin
implementation(libs.androidx.appcompat)
```

And to `gradle/libs.versions.toml`:

```toml
[versions]
appcompat = "1.7.0"

[libraries]
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
```

## Business Rules

- BR-01: All screens must be wrapped in `GymLibraryTheme`
- BR-02: Hardcoded colors must not appear outside `Color.kt`
- BR-03: `res/values/themes.xml` must use `Theme.AppCompat.Light.NoActionBar` as parent — `android:Theme.Material.NoActionBar` (AOSP Material 1) causes incorrect `WindowInsetsController` behavior on OEM devices (Samsung, Xiaomi) running API 26–28. Requires `androidx.appcompat:appcompat` dependency.
- BR-04: Typography val must be named `AppTypography` — naming it `Typography` shadows `androidx.compose.material3.Typography` class within the same package, breaking any future custom typography declarations
