package com.zalamena.condominios.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary
private val Primary = Color(0xFF4A6B5D)
private val OnPrimary = Color(0xFFFFFFFF)
private val PrimaryContainer = Color(0xFFCCE8DA)
private val OnPrimaryContainer = Color(0xFF06201A)

// Secondary
private val Secondary = Color(0xFF6B5D4A)
private val OnSecondary = Color(0xFFFFFFFF)
private val SecondaryContainer = Color(0xFFF3DFC2)
private val OnSecondaryContainer = Color(0xFF24190B)

// Tertiary
private val Tertiary = Color(0xFF4A6080)
private val OnTertiary = Color(0xFFFFFFFF)
private val TertiaryContainer = Color(0xFFD0E4FF)
private val OnTertiaryContainer = Color(0xFF001D36)

// Error
private val ErrorColor = Color(0xFFBA1A1A)
private val OnError = Color(0xFFFFFFFF)
private val ErrorContainer = Color(0xFFFFDAD6)
private val OnErrorContainer = Color(0xFF410002)

// Surface
private val Surface = Color(0xFFF8FAF6)
private val OnSurface = Color(0xFF191C1A)
private val SurfaceVariant = Color(0xFFDCE5DD)
private val OnSurfaceVariant = Color(0xFF404943)

// Surface containers
private val SurfaceContainerLowest = Color(0xFFFFFFFF)
private val SurfaceContainerLow = Color(0xFFF2F5F0)
private val SurfaceContainer = Color(0xFFECF0EB)
private val SurfaceContainerHigh = Color(0xFFE7EAE5)
private val SurfaceContainerHighest = Color(0xFFE1E3DF)

// Outline
private val Outline = Color(0xFF707973)
private val OutlineVariant = Color(0xFFC0C9C1)

// Inverse
private val InverseSurface = Color(0xFF2E312E)
private val InverseOnSurface = Color(0xFFEFF1ED)

// Background
private val Background = Color(0xFFF8FAF6)
private val OnBackground = Color(0xFF191C1A)

val CondominiosLightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = ErrorColor,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    background = Background,
    onBackground = OnBackground
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
