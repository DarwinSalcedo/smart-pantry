package com.smart.pantry.presentation.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spacing system for consistent layout
 * Based on 8dp grid system
 */
object Spacing {
    val none: Dp = 0.dp
    val extraSmall: Dp = 4.dp
    val small: Dp = 8.dp
    val medium: Dp = 16.dp
    val large: Dp = 24.dp
    val extraLarge: Dp = 32.dp
    val huge: Dp = 48.dp
    val massive: Dp = 64.dp
}

/**
 * Padding values for common components
 */
object Padding {
    val card = Spacing.medium
    val screen = Spacing.medium
    val button = Spacing.small
    val icon = Spacing.small
}

/**
 * Corner radius values
 */
object CornerRadius {
    val small: Dp = 4.dp
    val medium: Dp = 8.dp
    val large: Dp = 16.dp
    val extraLarge: Dp = 24.dp
    val round: Dp = 999.dp
}

/**
 * Elevation values for Material Design
 */
object Elevation {
    val none: Dp = 0.dp
    val small: Dp = 2.dp
    val medium: Dp = 4.dp
    val large: Dp = 8.dp
    val extraLarge: Dp = 16.dp
}
