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
- `core/ui/theme/Type.kt` — `Typography` definition

## Business Rules

- BR-01: All screens must be wrapped in `GymLibraryTheme`
- BR-02: Hardcoded colors must not appear outside `Color.kt`
