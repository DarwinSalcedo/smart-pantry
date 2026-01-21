package com.smart.pantry.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.smart.pantry.presentation.ui.theme.*

/**
 * Animated Fridge Component with 3D wire shelves
 */
@Composable
fun AnimatedFridge(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Door rotation animation
    val doorRotation by animateFloatAsState(
        targetValue = if (isOpen) -90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        visibilityThreshold = 0.8f,
        label = "door_rotation"
    )

    // Interior light animation
    val lightAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "light_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Fridge body with 3D interior
        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(Elevation.large, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = FridgeInterior
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium)
            ) {
                // 3D Wire shelves background
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawFridgeInterior(lightAlpha)
                }

                // Interior light effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = lightAlpha * 0.4f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Content (items inside fridge)
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        content()
                    }
                }
            }
        }

        // Fridge door with 3D rotation
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = doorRotation
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                    cameraDistance = 12f * density
                }
                .shadow(Elevation.extraLarge, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = FridgeColor
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium),
                contentAlignment = Alignment.Center
            ) {
                // Door handle (metallic)
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(100.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-24).dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFBDBDBD),
                                    Color(0xFFE0E0E0),
                                    Color(0xFFBDBDBD)
                                )
                            ),
                            RoundedCornerShape(CornerRadius.small)
                        )
                        .shadow(Elevation.small, RoundedCornerShape(CornerRadius.small))
                )

                // Fridge brand/logo area
                Text(
                    text = "FRIDGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray400,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Spacing.large)
                )
            }
        }
    }
}

/**
 * Draw 3D fridge interior with wire shelves
 */
private fun DrawScope.drawFridgeInterior(lightAlpha: Float) {
    val width = size.width
    val height = size.height
    
    // Draw 3 wire shelves with perspective
    val shelfPositions = listOf(0.25f, 0.5f, 0.75f)
    
    shelfPositions.forEach { position ->
        val y = height * position
        
        // Shelf depth effect (perspective)
        val shelfDepth = 40f
        
        // Back edge of shelf (darker)
        drawLine(
            color = Color(0xFF9E9E9E).copy(alpha = 0.6f + lightAlpha * 0.4f),
            start = Offset(shelfDepth, y - 2),
            end = Offset(width - shelfDepth, y - 2),
            strokeWidth = 3f
        )
        
        // Front edge of shelf (lighter)
        drawLine(
            color = Color(0xFFBDBDBD).copy(alpha = 0.8f + lightAlpha * 0.2f),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 4f
        )
        
        // Wire grid pattern
        for (i in 0..8) {
            val x = (width / 8) * i
            drawLine(
                color = FridgeMetal.copy(alpha = 0.3f + lightAlpha * 0.3f),
                start = Offset(x, y - 2),
                end = Offset(x + shelfDepth / 2, y),
                strokeWidth = 1.5f
            )
        }
        
        // Side depth lines
        drawLine(
            color = Color(0xFF757575).copy(alpha = 0.5f + lightAlpha * 0.3f),
            start = Offset(0f, y),
            end = Offset(shelfDepth, y - 2),
            strokeWidth = 2f
        )
        drawLine(
            color = Color(0xFF757575).copy(alpha = 0.5f + lightAlpha * 0.3f),
            start = Offset(width, y),
            end = Offset(width - shelfDepth, y - 2),
            strokeWidth = 2f
        )
    }
    
    // Back wall grid pattern
    for (i in 0..10) {
        val x = (width / 10) * i
        drawLine(
            color = Color(0xFFE0E0E0).copy(alpha = 0.2f + lightAlpha * 0.2f),
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
        )
    }
}

/**
 * Animated Pantry Component with wooden shelves
 */
@Composable
fun AnimatedPantry(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Left door slide animation
    val leftDoorOffset by animateDpAsState(
        targetValue = if (isOpen) (-120).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "left_door_offset"
    )

    // Right door slide animation
    val rightDoorOffset by animateDpAsState(
        targetValue = if (isOpen) 120.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "right_door_offset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Pantry body with wooden shelves
        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(Elevation.large, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = PantryInterior
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium)
            ) {
                // 3D Wooden shelves background
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawPantryInterior()
                }
                
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Content (items inside pantry)
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        content()
                    }
                }
            }
        }

        // Left sliding door
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(140.dp)
                .align(Alignment.CenterStart)
                .offset(x = leftDoorOffset)
                .shadow(Elevation.medium, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = PantryColor
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.small),
                contentAlignment = Alignment.Center
            ) {
                // Door handle
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(60.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-12).dp)
                        .background(
                            Color(0xFF9E9E9E),
                            RoundedCornerShape(CornerRadius.small)
                        )
                )
            }
        }

        // Right sliding door
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(140.dp)
                .align(Alignment.CenterEnd)
                .offset(x = rightDoorOffset)
                .shadow(Elevation.medium, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = PantryColor
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.small),
                contentAlignment = Alignment.Center
            ) {
                // Door handle
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .height(60.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = 12.dp)
                        .background(
                            Color(0xFF9E9E9E),
                            RoundedCornerShape(CornerRadius.small)
                        )
                )
            }
        }
    }
}

/**
 * Draw 3D pantry interior with wooden shelves
 */
private fun DrawScope.drawPantryInterior() {
    val width = size.width
    val height = size.height
    
    // Draw 4 wooden shelves with perspective
    val shelfPositions = listOf(0.2f, 0.4f, 0.6f, 0.8f)
    
    shelfPositions.forEach { position ->
        val y = height * position
        val shelfDepth = 50f
        val shelfThickness = 12f
        
        // Shelf 3D effect
        val shelfPath = Path().apply {
            // Front face
            moveTo(0f, y)
            lineTo(width, y)
            lineTo(width, y + shelfThickness)
            lineTo(0f, y + shelfThickness)
            close()
        }
        
        // Draw shelf with wood gradient
        drawPath(
            path = shelfPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    PantryWood,
                    PantryWood.copy(alpha = 0.8f)
                ),
                startY = y,
                endY = y + shelfThickness
            )
        )
        
        // Top edge (lighter)
        drawLine(
            color = PantryWood.copy(alpha = 1f),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 2f
        )
        
        // Bottom edge (darker for depth)
        drawLine(
            color = Color(0xFFBCAAA4),
            start = Offset(0f, y + shelfThickness),
            end = Offset(width, y + shelfThickness),
            strokeWidth = 2f
        )
        
        // Wood grain texture
        for (i in 0..20) {
            val x = (width / 20) * i + (i % 3) * 5
            drawLine(
                color = Color(0xFFA1887F).copy(alpha = 0.2f),
                start = Offset(x, y),
                end = Offset(x, y + shelfThickness),
                strokeWidth = 1f
            )
        }
        
        // Vertical dividers for depth
        val dividerPositions = listOf(0.33f, 0.66f)
        dividerPositions.forEach { divPos ->
            val x = width * divPos
            drawLine(
                color = Color(0xFF8D6E63).copy(alpha = 0.4f),
                start = Offset(x, y),
                end = Offset(x, y + shelfThickness),
                strokeWidth = 3f
            )
        }
    }
}

/**
 * Animated Freezer (similar to fridge but with frost effect)
 */
@Composable
fun AnimatedFreezer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Door rotation animation
    val doorRotation by animateFloatAsState(
        targetValue = if (isOpen) -90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "freezer_door_rotation"
    )

    val lightAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "freezer_light_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // Freezer body
        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(Elevation.large, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = FreezerFrost
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium)
            ) {
                // 3D Wire shelves with frost
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawFreezerInterior(lightAlpha)
                }

                // Cold air effect
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF034576).copy(alpha = lightAlpha * 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = isOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        content()
                    }
                }
            }
        }

        // Freezer door
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = doorRotation
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                    cameraDistance = 12f * density
                }
                .shadow(Elevation.extraLarge, RoundedCornerShape(CornerRadius.medium)),
            colors = CardDefaults.cardColors(
                containerColor = FreezerColor
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.medium),
                contentAlignment = Alignment.Center
            ) {
                // Door handle
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(100.dp)
                        .align(Alignment.CenterEnd)
                        .offset(x = (-24).dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    FreezerMetal,
                                    Color(0xFFE0E0E0),
                                    FreezerMetal
                                )
                            ),
                            RoundedCornerShape(CornerRadius.small)
                        )
                )

                Text(
                    text = "FREEZER",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray400,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = Spacing.large)
                )
            }
        }
    }
}

/**
 * Draw freezer interior with wire shelves and frost
 */
private fun DrawScope.drawFreezerInterior(lightAlpha: Float) {
    val width = size.width
    val height = size.height
    
    // Draw wire shelves (similar to fridge but with frost effect)
    val shelfPositions = listOf(0.25f, 0.5f, 0.75f)
    
    shelfPositions.forEach { position ->
        val y = height * position
        val shelfDepth = 40f
        
        // Frosty back edge
        drawLine(
            color = Color(0xFFB0BEC5).copy(alpha = 0.7f + lightAlpha * 0.3f),
            start = Offset(shelfDepth, y - 2),
            end = Offset(width - shelfDepth, y - 2),
            strokeWidth = 3f
        )
        
        // Front edge
        drawLine(
            color = FreezerMetal.copy(alpha = 0.9f + lightAlpha * 0.1f),
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 4f
        )
        
        // Wire grid with frost
        for (i in 0..8) {
            val x = (width / 8) * i
            drawLine(
                color = Color(0xFFCFD8DC).copy(alpha = 0.4f + lightAlpha * 0.3f),
                start = Offset(x, y - 2),
                end = Offset(x + shelfDepth / 2, y),
                strokeWidth = 1.5f
            )
        }
        
        // Frost particles effect
        for (i in 0..15) {
            val x = (width / 15) * i + (i % 3) * 10
            val frostY = y + (i % 5) * 3f
            drawCircle(
                color = Color.White.copy(alpha = 0.3f + lightAlpha * 0.2f),
                radius = 2f,
                center = Offset(x, frostY)
            )
        }
    }
}
