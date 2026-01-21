package com.smart.pantry.presentation.ui.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smart.pantry.presentation.ui.components.*
import com.smart.pantry.presentation.ui.theme.SmartPantryTheme
import com.smart.pantry.presentation.ui.theme.Spacing

/**
 * Demo screen showcasing the animated storage visualization
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryDemoScreen() {
    SmartPantryTheme {
        var selectedStorage by remember { mutableStateOf(StorageType.FRIDGE) }

        // Sample data
        val sampleItems = listOf(
            StorageItem(
                id = "1",
                name = "Leche",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-25"
            ),
            StorageItem(
                id = "2",
                name = "Yogurt",
                status = ItemStatus.EXPIRING_SOON,
                type = StorageType.FRIDGE,
                expirationDate = "2026-01-22"
            ),
            StorageItem(
                id = "3",
                name = "Queso",
                status = ItemStatus.EXPIRED,
                type = StorageType.FRIDGE,
                expirationDate = "2026-01-18"
            ),
            StorageItem(
                id = "4",
                name = "Mantequilla",
                status = ItemStatus.FRESH,
                type = StorageType.FREEZER,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "5",
                name = "Rice",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-25"
            ),
            StorageItem(
                id = "6",
                name = "Pasta",
                status = ItemStatus.EXPIRING_SOON,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-22"
            ),
            StorageItem(
                id = "7",
                name = "Chicken",
                status = ItemStatus.EXPIRED,
                type = StorageType.FREEZER,
                expirationDate = "2026-01-18"
            ),
            StorageItem(
                id = "8",
                name = "Fish",
                status = ItemStatus.FRESH,
                type = StorageType.FREEZER,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "9",
                name = "Tuna Can",
                status = ItemStatus.EXPIRING_SOON,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-22"
            ),
            StorageItem(
                id = "10",
                name = "Banana",
                status = ItemStatus.EXPIRED,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-18"
            ),
            StorageItem(
                id = "11",
                name = "Eggs",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "12",
                name = "Tuna Can",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-22"
            ),
            StorageItem(
                id = "13",
                name = "Banana",
                status = ItemStatus.EXPIRED,
                type = StorageType.PANTRY,
                expirationDate = "2026-01-18"
            ),
            StorageItem(
                id = "14",
                name = "Eggs",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "15",
                name = "Wine",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "16",
                name = "Bread",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-02-01"
            ),
            StorageItem(
                id = "17",
                name = "Other",
                status = ItemStatus.FRESH,
                type = StorageType.PANTRY,
                expirationDate = "2026-02-01"
            ),
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("SmartPantry") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                StorageVisualization(
                    selectedStorage = selectedStorage,
                    items = sampleItems.filter { 
                         it.type == selectedStorage
                    },
                    onStorageChange = { selectedStorage = it },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
