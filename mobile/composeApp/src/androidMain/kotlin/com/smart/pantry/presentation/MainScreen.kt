package com.smart.pantry.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smart.pantry.presentation.navigation.AppNavigation
import com.smart.pantry.presentation.navigation.Screen

/**
 * Main app screen with bottom navigation
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Bottom navigation item data class
 */
data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

/**
 * Bottom navigation items
 */
val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Pantry,
        label = "Pantry",
        icon = Icons.Default.Kitchen
    ),
    BottomNavItem(
        screen = Screen.Nutrition,
        label = "Nutrition",
        icon = Icons.Default.Restaurant
    ),
    BottomNavItem(
        screen = Screen.Scanner,
        label = "Scan",
        icon = Icons.Default.QrCodeScanner
    ),
    BottomNavItem(
        screen = Screen.Settings,
        label = "Settings",
        icon = Icons.Default.Settings
    )
)
