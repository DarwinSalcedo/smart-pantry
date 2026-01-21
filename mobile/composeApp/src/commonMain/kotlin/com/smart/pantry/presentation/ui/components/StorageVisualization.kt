package com.smart.pantry.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.smart.pantry.presentation.ui.theme.*

/**
 * Storage visualization with animated transitions between storage types
 */
@Composable
fun StorageVisualization(
    selectedStorage: StorageType,
    items: List<StorageItem>,
    onStorageChange: (StorageType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }

    LaunchedEffect(selectedStorage) {
        isOpen = false
        kotlinx.coroutines.delay(300)
        isOpen = true
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Storage selector tabs
        StorageSelector(
            selectedStorage = selectedStorage,
            onStorageChange = {
                onStorageChange(it)
            }
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        // Animated storage container
        AnimatedContent(
            targetState = selectedStorage,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) +
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { if (targetState.ordinal > initialState.ordinal) 300 else -300 }
                        ) togetherWith
                        fadeOut(animationSpec = tween(300)) +
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { if (targetState.ordinal > initialState.ordinal) -300 else 300 }
                        )
            },
            label = "storage_transition"
        ) { storage ->
            when (storage) {
                StorageType.FRIDGE -> {
                    AnimatedFridge(
                        isOpen = isOpen,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ItemsList(items = items)
                    }
                }
                StorageType.PANTRY -> {
                    AnimatedPantry(
                        isOpen = isOpen,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ItemsList(items = items)
                    }
                }
                StorageType.FREEZER -> {
                    AnimatedFreezer(
                        isOpen = isOpen,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ItemsList(items = items)
                    }
                }
            }
        }
    }
}

/**
 * Storage selector with animated indicator
 */
@Composable
fun StorageSelector(
    selectedStorage: StorageType,
    onStorageChange: (StorageType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StorageType.values().forEach { storage ->
            StorageTab(
                storage = storage,
                isSelected = selectedStorage == storage,
                onClick = { onStorageChange(storage) }
            )
        }
    }
}

@Composable
fun StorageTab(
    storage: StorageType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tab_scale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier.scale(scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                when (storage) {
                    StorageType.FRIDGE -> FridgeColor
                    StorageType.PANTRY -> PantryColor
                    StorageType.FREEZER -> FreezerColor
                }
            } else {
                Gray300
            },
            contentColor = if (isSelected) Gray800 else Gray600
        )
    ) {
        Text(
            text = when (storage) {
                StorageType.FRIDGE -> "Nevera"
                StorageType.PANTRY -> "Alacena"
                StorageType.FREEZER -> "Congelador"
            }
        )
    }
}

/**
 * Animated freezer (similar to fridge but with cyan theme)

@Composable
fun AnimatedFreezer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Similar to AnimatedFridge but with freezer colors
    AnimatedFridge(
        isOpen = isOpen,
        modifier = modifier,
        content = content
    )
} */

/**
 * Items list with stagger animation
 */
@Composable
fun ItemsList(
    items: List<StorageItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.small),
        contentPadding = PaddingValues(Spacing.medium)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            AnimatedItemCard(
                itemName = item.name,
                expirationStatus = item.status,
               /* modifier = Modifier.animateItemPlacement(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )*/
            )
        }
    }
}

/**
 * Data class for storage items
 */
data class StorageItem(
    val id: String,
    val name: String,
    val status: ItemStatus,
    val type: StorageType,
    val expirationDate: String
)
