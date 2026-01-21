package com.smart.pantry.data.local.entity

/**
 * User entity for local database
 */
data class UserEntity(
    val id: String,
    val email: String,
    val name: String?,
    val createdAt: Long,
    val updatedAt: Long
)

/**
 * Product entity - Global catalog
 */
data class ProductEntity(
    val id: String,
    val name: String,
    val category: String,
    val defaultShelfLifeDays: Int,
    val barcode: String?,
    val imageUrl: String?,
    val nutritionInfo: String?, // JSON string
    val createdAt: Long
)

/**
 * Pantry item entity - User's inventory
 */
data class PantryItemEntity(
    val id: String,
    val userId: String,
    val productId: String,
    val productName: String, // Denormalized for quick access
    val quantity: Int,
    val unit: String, // "units", "kg", "liters", etc.
    val location: String, // "FRIDGE", "PANTRY", "FREEZER"
    val purchaseDate: Long,
    val expirationDate: Long,
    val notes: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncStatus: String // "SYNCED", "PENDING", "ERROR"
)

/**
 * Nutrition log entity - Daily consumption tracking
 */
data class NutritionLogEntity(
    val id: String,
    val userId: String,
    val mealDate: Long, // Date only (timestamp at 00:00)
    val mealType: String, // "BREAKFAST", "LUNCH", "DINNER", "SNACK"
    val calories: Int,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val imageUrl: String?,
    val notes: String?,
    val createdAt: Long,
    val syncStatus: String
)

/**
 * Receipt entity - Scanned receipts
 */
data class ReceiptEntity(
    val id: String,
    val userId: String,
    val imageUrl: String,
    val scanDate: Long,
    val processed: Boolean,
    val geminiResponse: String?, // JSON string
    val createdAt: Long,
    val syncStatus: String
)

/**
 * Pending sync entity - Queue for offline operations
 */
data class PendingSyncEntity(
    val id: String,
    val entityType: String, // "PANTRY_ITEM", "NUTRITION_LOG", etc.
    val entityId: String,
    val operation: String, // "CREATE", "UPDATE", "DELETE"
    val payload: String, // JSON string
    val createdAt: Long,
    val retryCount: Int,
    val lastError: String?
)
