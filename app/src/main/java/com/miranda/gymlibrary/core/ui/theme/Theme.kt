package com.miranda.gymlibrary.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Red700,
    onPrimary = OnBackground,
    background = Background,
    surface = Surface,
    onBackground = OnBackground,
    onSurface = OnSurface
)

@Composable
fun GymLibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
