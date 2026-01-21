package com.smart.pantry.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = PrimaryGreenLight,
    onPrimaryContainer = Color.Black,
    
    secondary = SecondaryBlue,
    onSecondary = Color.White,
    secondaryContainer = SecondaryBlueLight,
    onSecondaryContainer = Color.Black,
    
    tertiary = AccentOrange,
    onTertiary = Color.White,
    tertiaryContainer = AccentOrange.copy(alpha = 0.3f),
    onTertiaryContainer = Color.Black,
    
    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.3f),
    onErrorContainer = Color.Black,
    
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    outline = Gray400,
    outlineVariant = Gray200
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreenLight,
    onPrimary = Color.Black,
    primaryContainer = PrimaryGreenDark,
    onPrimaryContainer = Color.White,
    
    secondary = SecondaryBlueLight,
    onSecondary = Color.Black,
    secondaryContainer = SecondaryBlueDark,
    onSecondaryContainer = Color.White,
    
    tertiary = AccentOrange,
    onTertiary = Color.Black,
    tertiaryContainer = AccentOrange.copy(alpha = 0.3f),
    onTertiaryContainer = Color.White,
    
    error = Error,
    onError = Color.Black,
    errorContainer = Error.copy(alpha = 0.3f),
    onErrorContainer = Color.White,
    
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    
    surfaceVariant = Gray800,
    onSurfaceVariant = Gray300,
    outline = Gray600,
    outlineVariant = Gray700
)

@Composable
fun SmartPantryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
