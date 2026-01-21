package com.smart.pantry.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smart.pantry.presentation.nutrition.NutritionScreen
import com.smart.pantry.presentation.scanner.ScannerScreen
import com.smart.pantry.presentation.settings.SettingsScreen
import com.smart.pantry.presentation.pantry.PantryScreen2

/**
 * Navigation graph for the app
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Pantry.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Pantry.route) {
            PantryScreen2()
        }
        
        composable(Screen.Nutrition.route) {
            NutritionScreen()
        }
        
        composable(Screen.Scanner.route) {
            ScannerScreen()
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
