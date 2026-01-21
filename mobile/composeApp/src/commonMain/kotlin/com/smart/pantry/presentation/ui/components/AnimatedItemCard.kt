package com.smart.pantry.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smart.pantry.presentation.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Animated item card with status-based animations
 */
@Composable
fun AnimatedItemCard(
    itemName: String,
    expirationStatus: ItemStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    // Pulse animation for expiring items
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (expirationStatus == ItemStatus.EXPIRING_SOON) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Shake animation for expired items
    val shakeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (expirationStatus == ItemStatus.EXPIRED) 5f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shake_offset"
    )

    // Entry animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(50)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) +
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
        exit = fadeOut() + scaleOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .scale(pulseScale)
                .offset(x = shakeOffset.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (expirationStatus) {
                    ItemStatus.FRESH -> StatusFresh.copy(alpha = 0.1f)
                    ItemStatus.EXPIRING_SOON -> StatusExpiringSoon.copy(alpha = 0.2f)
                    ItemStatus.EXPIRED -> StatusExpired.copy(alpha = 0.2f)
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = Elevation.small
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyLarge
                )

                // Status indicator
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(
                            when (expirationStatus) {
                                ItemStatus.FRESH -> StatusFresh
                                ItemStatus.EXPIRING_SOON -> StatusExpiringSoon
                                ItemStatus.EXPIRED -> StatusExpired
                            }
                        )
                )
            }
        }
    }
}

/**
 * Item status enum
 */
enum class ItemStatus {
    FRESH,
    EXPIRING_SOON,
    EXPIRED
}

/**
 * Animated item entering the storage (flying animation)
 */
@Composable
fun FlyingItemAnimation(
    itemName: String,
    targetStorage: StorageType,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var animationStarted by remember { mutableStateOf(false) }
    
    val offsetY by animateFloatAsState(
        targetValue = if (animationStarted) 400f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { onAnimationEnd() },
        label = "flying_offset"
    )

    val scale by animateFloatAsState(
        targetValue = if (animationStarted) 0.5f else 1f,
        animationSpec = tween(800),
        label = "flying_scale"
    )

    LaunchedEffect(Unit) {
        delay(100)
        animationStarted = true
    }

    Box(
        modifier = modifier
            .offset(y = offsetY.dp)
            .scale(scale)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = when (targetStorage) {
                    StorageType.FRIDGE -> FridgeColor
                    StorageType.PANTRY -> PantryColor
                    StorageType.FREEZER -> FreezerColor
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = Elevation.large
            )
        ) {
            Text(
                text = itemName,
                modifier = Modifier.padding(Spacing.medium),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

/**
 * Storage type enum
 */
enum class StorageType {
    FRIDGE,
    PANTRY,
    FREEZER
}

/**
 * Shimmer loading effect
 */
@Composable
fun ShimmerLoadingCard(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray200.copy(alpha = shimmerAlpha)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize())
    }
}
