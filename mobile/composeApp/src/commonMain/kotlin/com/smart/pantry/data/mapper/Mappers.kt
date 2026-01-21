package com.smart.pantry.data.mapper

import com.smart.pantry.data.local.entity.NutritionLogEntity
import com.smart.pantry.data.local.entity.PantryItemEntity
import com.smart.pantry.data.local.entity.PendingSyncEntity
import com.smart.pantry.data.local.entity.ProductEntity
import com.smart.pantry.database.NutritionLog
import com.smart.pantry.database.PantryItem
import com.smart.pantry.database.PendingSync
import com.smart.pantry.database.Product

fun NutritionLog.toEntity() = NutritionLogEntity(
    id = id,
    userId = userId,
    mealDate = mealDate,
    mealType = mealType,
    calories = calories.toInt(),
    proteinG = proteinG,
    carbsG = carbsG,
    fatG = fatG,
    imageUrl = imageUrl,
    notes = notes,
    createdAt = createdAt,
    syncStatus = syncStatus
)


fun PantryItem.toEntity() = PantryItemEntity(
    id = id,
    userId = userId,
    productId = productId,
    productName = productName,
    quantity = quantity.toInt(),
    unit = unit,
    location = location,
    purchaseDate = purchaseDate,
    expirationDate = expirationDate,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus
)

fun PendingSync.toEntity() = PendingSyncEntity(
    id = id,
    entityType = entityType,
    entityId = entityId,
    operation = operation,
    payload = payload,
    createdAt = createdAt,
    retryCount = retryCount.toInt(),
    lastError = lastError
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    category = category,
    defaultShelfLifeDays = defaultShelfLifeDays.toInt(),
    barcode = barcode,
    imageUrl = imageUrl,
    nutritionInfo = nutritionInfo,
    createdAt = createdAt
)

