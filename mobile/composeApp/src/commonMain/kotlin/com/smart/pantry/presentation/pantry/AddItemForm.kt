package com.smart.pantry.presentation.pantry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smart.pantry.domain.model.*
import kotlinx.datetime.*

/**
 * Form to add a new item to pantry
 * Designed for accessibility and ease of use for all ages
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemForm(
    onSave: (AddItemData) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var productName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ProductCategory.OTHER) }
    var quantity by remember { mutableIntStateOf(1) }
    var selectedUnit by remember { mutableStateOf(MeasurementUnit.UNITS) }
    var selectedLocation by remember { mutableStateOf(StorageLocation.FRIDGE) }
    var expirationDate by remember { mutableLongStateOf(getDefaultExpirationDate()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    
    val isValid = productName.trim().length >= 2

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        Text(
            text = "Add New Item",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Divider()
        
        // Product Name
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Product Name *", style = MaterialTheme.typography.bodyLarge) },
            placeholder = { Text("e.g., Milk, Eggs, Bread") },
            leadingIcon = {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            isError = productName.isNotEmpty() && !isValid
        )
        
        if (productName.isNotEmpty() && !isValid) {
            Text(
                text = "Product name must be at least 2 characters",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        
        // Category Selector
        Text(
            text = "Category",
            style = MaterialTheme.typography.titleMedium
        )
        
        ExposedDropdownMenuBox(
            expanded = showCategoryMenu,
            onExpandedChange = { showCategoryMenu = it }
        ) {
            OutlinedTextField(
                value = getCategoryDisplayName(selectedCategory),
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Category") },
                leadingIcon = {
                    Text(getCategoryIcon(selectedCategory), style = MaterialTheme.typography.headlineSmall)
                },
                trailingIcon = {
                    Icon(
                        if (showCategoryMenu) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .menuAnchor(),
                textStyle = MaterialTheme.typography.bodyLarge
            )
            
            ExposedDropdownMenu(
                expanded = showCategoryMenu,
                onDismissRequest = { showCategoryMenu = false }
            ) {
                ProductCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = getCategoryIcon(category),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = getCategoryDisplayName(category),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        },
                        onClick = {
                            selectedCategory = category
                            showCategoryMenu = false
                        },
                        modifier = Modifier.height(56.dp)
                    )
                }
            }
        }
        
        // Quantity
        Text(
            text = "Quantity",
            style = MaterialTheme.typography.titleMedium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrease button
            FilledIconButton(
                onClick = { if (quantity > 1) quantity-- },
                modifier = Modifier.size(56.dp),
                enabled = quantity > 1
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Decrease quantity",
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Quantity display
            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = { 
                    it.toIntOrNull()?.let { newQty ->
                        if (newQty in 1..99) quantity = newQty
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            // Increase button
            FilledIconButton(
                onClick = { if (quantity < 99) quantity++ },
                modifier = Modifier.size(56.dp),
                enabled = quantity < 99
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase quantity",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Unit Selector
        Text(
            text = "Unit",
            style = MaterialTheme.typography.titleMedium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                MeasurementUnit.UNITS,
                MeasurementUnit.KILOGRAMS,
                MeasurementUnit.GRAMS,
                MeasurementUnit.LITERS
            ).forEach { unit ->
                FilterChip(
                    selected = selectedUnit == unit,
                    onClick = { selectedUnit = unit },
                    label = { 
                        Text(
                            unit.displayName,
                            style = MaterialTheme.typography.bodyLarge
                        ) 
                    },
                    modifier = Modifier.height(48.dp)
                )
            }
        }
        
        // Storage Location
        Text(
            text = "Storage Location",
            style = MaterialTheme.typography.titleMedium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StorageLocation.entries.forEach { location ->
                FilterChip(
                    selected = selectedLocation == location,
                    onClick = { selectedLocation = location },
                    label = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (location) {
                                    StorageLocation.FRIDGE -> Icons.Default.Kitchen
                                    StorageLocation.PANTRY -> Icons.Default.Inventory
                                    StorageLocation.FREEZER -> Icons.Default.AcUnit
                                },
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                location.name.lowercase().capitalize(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }
        }
        
        // Expiration Date
        Text(
            text = "Expiration Date",
            style = MaterialTheme.typography.titleMedium
        )
        
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                formatDate(expirationDate),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text("Cancel", style = MaterialTheme.typography.titleMedium)
            }
            
            Button(
                onClick = {
                    onSave(
                        AddItemData(
                            productName = productName.trim(),
                            category = selectedCategory,
                            quantity = quantity,
                            unit = selectedUnit,
                            location = selectedLocation,
                            expirationDate = expirationDate
                        )
                    )
                },
                enabled = isValid,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add to Pantry", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    
    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = expirationDate
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        expirationDate = it
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Data class for add item form
 */
data class AddItemData(
    val productName: String,
    val category: ProductCategory,
    val quantity: Int,
    val unit: MeasurementUnit,
    val location: StorageLocation,
    val expirationDate: Long
)

/**
 * Get default expiration date (7 days from now)
 */
private fun getDefaultExpirationDate(): Long {
    val now = Clock.System.now()
    val sevenDaysLater = now.plus(7, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
    return sevenDaysLater.toEpochMilliseconds()
}

/**
 * Get category display name
 */
private fun getCategoryDisplayName(category: ProductCategory): String {
    return category.name.lowercase().split('_').joinToString(" ") { 
        it.capitalize() 
    }
}

/**
 * Get category icon emoji
 */
private fun getCategoryIcon(category: ProductCategory): String {
    return when (category) {
        ProductCategory.FRUITS -> "🍎"
        ProductCategory.VEGETABLES -> "🥕"
        ProductCategory.DAIRY -> "🥛"
        ProductCategory.MEAT -> "🥩"
        ProductCategory.SEAFOOD -> "🐟"
        ProductCategory.GRAINS -> "🌾"
        ProductCategory.BAKERY -> "🍞"
        ProductCategory.BEVERAGES -> "🥤"
        ProductCategory.SNACKS -> "🍿"
        ProductCategory.CONDIMENTS -> "🧂"
        ProductCategory.FROZEN -> "❄️"
        ProductCategory.CANNED -> "🥫"
        ProductCategory.OTHER -> "📦"
    }
}

/**
 * Format date for display
 */
private fun formatDate(timestamp: Long): String {
    val instant = Instant.fromEpochMilliseconds(timestamp)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    return "${monthNames[localDate.monthNumber - 1]} ${localDate.dayOfMonth.toString().padStart(2, '0')}, ${localDate.year}"
}
