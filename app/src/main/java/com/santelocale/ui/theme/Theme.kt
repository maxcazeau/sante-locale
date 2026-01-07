package com.santelocale.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// ============================================================================
// HEADER DIMENSIONS - Unified across all screens for seamless transitions
// ============================================================================

/** Master header height used by all screens with curved headers */
val HeaderHeight = 260.dp

/** Header content top padding (positions back button and title consistently) */
val HeaderContentTopPadding = 48.dp

/** Header corner radius for the curved bottom edge */
val HeaderCornerRadius = 32.dp

/** Content overlap - how much the floating content overlaps the header */
val HeaderContentOverlap = 48.dp

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = AccentTeal,
    tertiary = Orange500,
    background = Slate50,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = Slate800,
    onSurface = Slate800
)

@Composable
fun SanteLocaleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // App is designed for light mode only

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = PrimaryGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
