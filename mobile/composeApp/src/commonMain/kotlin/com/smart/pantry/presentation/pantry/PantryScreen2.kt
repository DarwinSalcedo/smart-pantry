package com.smart.pantry.presentation.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smart.pantry.domain.model.StorageLocation
import com.smart.pantry.presentation.ui.components.*
import com.smart.pantry.presentation.ui.theme.SmartPantryTheme
import com.smart.pantry.presentation.ui.theme.Spacing
import org.koin.compose.koinInject

/**
 * Pantry screen with animated storage visualization
 * Connected to PantryViewModel and PantryUiState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen2(viewModel: PantryViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState()
    
    SmartPantryTheme {
        var selectedStorage by remember { mutableStateOf(StorageType.FRIDGE) }
        
        // Map domain StorageLocation to UI StorageType when selection changes
        LaunchedEffect(selectedStorage) {
            val domainLocation = when (selectedStorage) {
                StorageType.FRIDGE -> StorageLocation.FRIDGE
                StorageType.PANTRY -> StorageLocation.PANTRY
                StorageType.FREEZER -> StorageLocation.FREEZER
            }
            viewModel.selectLocation(domainLocation)
        }
        
        // Map domain items to UI items
        val currentItems = viewModel.getCurrentItems().map { pantryItem ->
            StorageItem(
                id = pantryItem.id,
                name = pantryItem.product.name,
                status = when (pantryItem.status) {
                    com.smart.pantry.domain.model.ItemStatus.FRESH -> ItemStatus.FRESH
                    com.smart.pantry.domain.model.ItemStatus.EXPIRING_SOON -> ItemStatus.EXPIRING_SOON
                    com.smart.pantry.domain.model.ItemStatus.EXPIRED -> ItemStatus.EXPIRED
                },
                type = when (pantryItem.location) {
                    StorageLocation.FRIDGE -> StorageType.FRIDGE
                    StorageLocation.PANTRY -> StorageType.PANTRY
                    StorageLocation.FREEZER -> StorageType.FREEZER
                },
                expirationDate = formatDate(pantryItem.expirationDate)
            )
        }

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
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    StorageVisualization(
                        selectedStorage = selectedStorage,
                        items = currentItems,
                        onStorageChange = { selectedStorage = it },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        
        // Show error if any
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // TODO: Show snackbar with error
                viewModel.clearError()
            }
        }
    }
}

/**
 * Format timestamp to date string
 */
private fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    return format.format(date)
}
