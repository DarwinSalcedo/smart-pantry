package com.smart.pantry.data.mapper

import com.smart.pantry.data.local.entity.*
import com.smart.pantry.database.NutritionLog
import com.smart.pantry.database.PantryItem
import com.smart.pantry.database.PendingSync
import com.smart.pantry.database.Product
import com.smart.pantry.domain.model.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

// ========== Database to Entity Mappers ==========

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

// ========== Entity to Domain Mappers ==========

fun ProductEntity.toDomain(): com.smart.pantry.domain.model.Product {
    val nutritionInfo = nutritionInfo?.let {
        try {
            Json.decodeFromString<NutritionInfo>(it)
        } catch (e: Exception) {
            null
        }
    }
    
    return com.smart.pantry.domain.model.Product(
        id = id,
        name = name,
        category = ProductCategory.fromString(category),
        defaultShelfLifeDays = defaultShelfLifeDays,
        barcode = barcode,
        imageUrl = imageUrl,
        nutritionInfo = nutritionInfo
    )
}

fun PantryItemEntity.toDomain(product: com.smart.pantry.domain.model.Product) = com.smart.pantry.domain.model.PantryItem(
    id = id,
    userId = userId,
    product = product,
    quantity = quantity,
    unit = MeasurementUnit.fromString(unit),
    location = StorageLocation.fromString(location),
    purchaseDate = purchaseDate,
    expirationDate = expirationDate,
    notes = notes,
    status = ItemStatus.fromExpirationDate(expirationDate)
)

fun NutritionLogEntity.toDomain() = com.smart.pantry.domain.model.NutritionLog(
    id = id,
    userId = userId,
    mealDate = mealDate,
    mealType = MealType.fromString(mealType),
    calories = calories,
    proteinG = proteinG,
    carbsG = carbsG,
    fatG = fatG,
    imageUrl = imageUrl,
    notes = notes
)

// ========== Domain to Entity Mappers ==========

fun com.smart.pantry.domain.model.Product.toEntity(createdAt: Long): ProductEntity {
    val nutritionInfoJson = nutritionInfo?.let {
        Json.encodeToString(it)
    }
    
    return ProductEntity(
        id = id,
        name = name,
        category = category.name,
        defaultShelfLifeDays = defaultShelfLifeDays,
        barcode = barcode,
        imageUrl = imageUrl,
        nutritionInfo = nutritionInfoJson,
        createdAt = createdAt
    )
}

fun com.smart.pantry.domain.model.PantryItem.toEntity(
    createdAt: Long,
    updatedAt: Long,
    syncStatus: String = "PENDING"
) = PantryItemEntity(
    id = id,
    userId = userId,
    productId = product.id,
    productName = product.name,
    quantity = quantity,
    unit = unit.name.lowercase(),
    location = location.name,
    purchaseDate = purchaseDate,
    expirationDate = expirationDate,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt,
    syncStatus = syncStatus
)

fun com.smart.pantry.domain.model.NutritionLog.toEntity(
    createdAt: Long,
    syncStatus: String = "PENDING"
) = NutritionLogEntity(
    id = id,
    userId = userId,
    mealDate = mealDate,
    mealType = mealType.name,
    calories = calories,
    proteinG = proteinG,
    carbsG = carbsG,
    fatG = fatG,
    imageUrl = imageUrl,
    notes = notes,
    createdAt = createdAt,
    syncStatus = syncStatus
)
