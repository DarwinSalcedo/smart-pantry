package com.smart.pantry.presentation.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.smart.pantry.domain.model.ItemStatus
import com.smart.pantry.domain.model.PantryItem
import com.smart.pantry.domain.model.StorageLocation
import org.koin.compose.koinInject

/**
 * Main Pantry Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantryScreen(
    viewModel: PantryViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Pantry") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Location Tabs
            LocationTabs(
                selectedLocation = uiState.selectedLocation,
                onLocationSelected = viewModel::selectLocation
            )
            
            // Alerts Section
            if (uiState.expiringItems.isNotEmpty() || uiState.expiredItems.isNotEmpty()) {
                AlertsSection(
                    expiringCount = uiState.expiringItems.size,
                    expiredCount = uiState.expiredItems.size
                )
            }
            
            // Items List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val currentItems = viewModel.getCurrentItems()
                if (currentItems.isEmpty()) {
                    EmptyState(location = uiState.selectedLocation)
                } else {
                    ItemsList(
                        items = currentItems,
                        onQuantityChange = viewModel::updateQuantity,
                        onDelete = viewModel::deleteItem
                    )
                }
            }
        }
        
        // Error Snackbar
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar
                viewModel.clearError()
            }
        }
    }
}

@Composable
fun LocationTabs(
    selectedLocation: StorageLocation,
    onLocationSelected: (StorageLocation) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedLocation.ordinal,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        StorageLocation.entries.forEach { location ->
            Tab(
                selected = selectedLocation == location,
                onClick = { onLocationSelected(location) },
                text = { Text(location.name) },
                icon = {
                    Icon(
                        imageVector = when (location) {
                            StorageLocation.FRIDGE -> Icons.Default.Kitchen
                            StorageLocation.PANTRY -> Icons.Default.Inventory
                            StorageLocation.FREEZER -> Icons.Default.AcUnit
                        },
                        contentDescription = location.name
                    )
                }
            )
        }
    }
}

@Composable
fun AlertsSection(
    expiringCount: Int,
    expiredCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (expiredCount > 0) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Expired",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$expiredCount item(s) expired",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
        
        if (expiringCount > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Expiring Soon",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$expiringCount item(s) expiring soon",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun ItemsList(
    items: List<PantryItem>,
    onQuantityChange: (String, Int) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it.id }) { item ->
            PantryItemCard(
                item = item,
                onQuantityChange = { newQuantity ->
                    onQuantityChange(item.id, newQuantity)
                },
                onDelete = { onDelete(item.id) }
            )
        }
    }
}

@Composable
fun PantryItemCard(
    item: PantryItem,
    onQuantityChange: (Int) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (item.status) {
                ItemStatus.EXPIRED -> MaterialTheme.colorScheme.errorContainer
                ItemStatus.EXPIRING_SOON -> MaterialTheme.colorScheme.tertiaryContainer
                ItemStatus.FRESH -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${item.quantity} ${item.unit.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Expires: ${formatDate(item.expirationDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onQuantityChange(item.quantity - 1) }) {
                    Icon(Icons.Default.Remove, "Decrease")
                }
                IconButton(onClick = { onQuantityChange(item.quantity + 1) }) {
                    Icon(Icons.Default.Add, "Increase")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }
        }
    }
}

@Composable
fun EmptyState(location: StorageLocation) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = "Empty",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No items in ${location.name.lowercase()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Add items to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return format.format(date)
}
