package com.nabha.telemedicine.core.design.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Dark color scheme (primary design) ────────────────────────────────────
private val NabhaDarkColorScheme = darkColorScheme(
    primary              = NabhaBlue400,
    onPrimary            = SurfaceDark,
    primaryContainer     = NabhaBlue800,
    onPrimaryContainer   = NabhaBlue200,
    secondary            = NabhaGreen400,
    onSecondary          = SurfaceDark,
    secondaryContainer   = NabhaGreen800,
    onSecondaryContainer = NabhaGreen200,
    tertiary             = NabhaSaffron400,
    onTertiary           = SurfaceDark,
    tertiaryContainer    = NabhaSaffron700,
    onTertiaryContainer  = NabhaSaffron100,
    background           = SurfaceDark,
    onBackground         = TextPrimary,
    surface              = SurfaceDark2,
    onSurface            = TextPrimary,
    surfaceVariant       = CardDark,
    onSurfaceVariant     = TextSecondary,
    outline              = OutlineDark,
    outlineVariant       = DividerDark,
    error                = NabhaRed500,
    onError              = SurfaceDark,
    errorContainer       = NabhaRed700,
    onErrorContainer     = NabhaRed100,
    inverseSurface       = TextPrimary,
    inverseOnSurface     = SurfaceDark,
    inversePrimary       = NabhaBlue700,
    scrim                = SurfaceDark
)

// ── Light color scheme ──────────────────────────────────────────────────────
private val NabhaLightColorScheme = lightColorScheme(
    primary              = NabhaBlue700,
    onPrimary            = CardLight,
    primaryContainer     = NabhaBlue100,
    onPrimaryContainer   = NabhaBlue900,
    secondary            = NabhaGreen700,
    onSecondary          = CardLight,
    secondaryContainer   = NabhaGreen100,
    onSecondaryContainer = NabhaGreen900,
    tertiary             = NabhaSaffron600,
    onTertiary           = CardLight,
    tertiaryContainer    = NabhaSaffron100,
    onTertiaryContainer  = NabhaSaffron700,
    background           = SurfaceLight,
    onBackground         = TextOnLight,
    surface              = CardLight,
    onSurface            = TextOnLight,
    surfaceVariant       = CardLight2,
    onSurfaceVariant     = TextOnLightSec,
    outline              = OutlineLight,
    outlineVariant       = DividerLight,
    error                = NabhaRed600,
    onError              = CardLight,
    errorContainer       = NabhaRed100,
    onErrorContainer     = NabhaRed700,
    inverseSurface       = SurfaceDark2,
    inverseOnSurface     = TextPrimary,
    inversePrimary       = NabhaBlue400,
    scrim                = SurfaceDark
)

// ── Composition local for app-wide dark mode check ─────────────────────────
val LocalIsDarkTheme = compositionLocalOf { true }

@Composable
fun NabhaTheme(
    darkTheme: Boolean = true, // Default: dark theme for government app aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NabhaDarkColorScheme else NabhaLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = NabhaTypography,
        shapes      = NabhaShapes,
        content     = content
    )
}
