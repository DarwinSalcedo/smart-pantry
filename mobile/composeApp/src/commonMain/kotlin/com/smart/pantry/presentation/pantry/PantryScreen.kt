package com.smart.pantry.presentation.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.pantry.domain.model.*
import com.smart.pantry.presentation.ui.components.*
import com.smart.pantry.presentation.ui.theme.SmartPantryTheme
import com.smart.pantry.presentation.ui.theme.Spacing
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.uuid.Uuid
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi

/**
 * Pantry screen with animated storage visualization
 * Connected to PantryViewModel and PantryUiState
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun PantryScreen(viewModel: PantryViewModel = koinInject()) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddItemSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
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
                    ItemStatus.FRESH -> com.smart.pantry.presentation.ui.components.ItemStatus.FRESH
                    ItemStatus.EXPIRING_SOON -> com.smart.pantry.presentation.ui.components.ItemStatus.EXPIRING_SOON
                    ItemStatus.EXPIRED -> com.smart.pantry.presentation.ui.components.ItemStatus.EXPIRED
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
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddItemSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Item",
                        modifier = Modifier.size(28.dp)
                    )
                }
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
        
        // Add Item Bottom Sheet
        if (showAddItemSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddItemSheet = false },
                sheetState = sheetState,
               // windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                AddItemForm(
                    onSave = { formData ->
                        // Create PantryItem from form data
                        val newItem = PantryItem(
                            id = Uuid.random().toString(),
                            userId = "current-user-id", // TODO: Get from auth
                            product = Product(
                                id = Uuid.random().toString(),
                                name = formData.productName,
                                category = formData.category,
                                defaultShelfLifeDays = 7,
                                barcode = null,
                                imageUrl = null,
                                nutritionInfo = null
                            ),
                            quantity = formData.quantity,
                            unit = formData.unit,
                            location = formData.location,
                            purchaseDate = Clock.System.now().toEpochMilliseconds(),
                            expirationDate = formData.expirationDate,
                            notes = null,
                            status = ItemStatus.fromExpirationDate(formData.expirationDate)
                        )
                        
                        // Add item via ViewModel
                        viewModel.addItem(newItem)
                        
                        showAddItemSheet = false
                    },
                    onCancel = { showAddItemSheet = false },
                    modifier = Modifier.fillMaxWidth()
                )
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
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(timestamp)
    val localDate = instant.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    return "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}-${localDate.dayOfMonth.toString().padStart(2, '0')}"
}
