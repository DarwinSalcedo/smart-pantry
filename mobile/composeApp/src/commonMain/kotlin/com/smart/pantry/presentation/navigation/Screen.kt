package com.smart.pantry.presentation.navigation

/**
 * Navigation routes for the app
 */
sealed class Screen(val route: String) {
    data object Pantry : Screen("pantry")
    data object Nutrition : Screen("nutrition")
    data object Scanner : Screen("scanner")
    data object Settings : Screen("settings")
}
