package com.zalamena.condominios.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SageGreen = Color(0xFF4A6B5D)
private val SageGreenLight = Color(0xFFCCE8DC)
private val OnSageGreen = Color(0xFFFFFFFF)
private val OnSageGreenLight = Color(0xFF1B3529)

private val Background = Color(0xFFFAFDF8)
private val Surface = Color(0xFFFFFFFF)
private val SurfaceContainer = Color(0xFFF0F4EF)
private val OnSurface = Color(0xFF1B1C1A)
private val OnSurfaceVariant = Color(0xFF43483F)
private val Outline = Color(0xFF73796E)
private val OutlineVariant = Color(0xFFC3C8BC)

private val ErrorColor = Color(0xFFBA1A1A)
private val OnError = Color(0xFFFFFFFF)
private val ErrorContainer = Color(0xFFFFDAD6)
private val OnErrorContainer = Color(0xFF410002)

val CondominiosLightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = OnSageGreen,
    primaryContainer = SageGreenLight,
    onPrimaryContainer = OnSageGreenLight,
    secondary = SageGreen,
    onSecondary = OnSageGreen,
    secondaryContainer = SageGreenLight,
    onSecondaryContainer = OnSageGreenLight,
    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainer = SurfaceContainer,
    surfaceContainerLow = SurfaceContainer,
    surfaceContainerHigh = Color(0xFFE8ECE6),
    outline = Outline,
    outlineVariant = OutlineVariant,
    error = ErrorColor,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)

@Composable
fun CondominiosTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CondominiosLightColorScheme,
        content = content
    )
}
