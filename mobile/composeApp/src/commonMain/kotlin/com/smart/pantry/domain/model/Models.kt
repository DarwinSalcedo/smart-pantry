package com.smart.pantry.domain.model

import kotlinx.serialization.Serializable

/**
 * Domain model for User
 * Clean domain object without database-specific details
 */
data class User(
    val id: String,
    val email: String,
    val name: String?
)

/**
 * Domain model for Product
 */
data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val defaultShelfLifeDays: Int,
    val barcode: String?,
    val imageUrl: String?,
    val nutritionInfo: NutritionInfo?
)

/**
 * Product categories
 */
enum class ProductCategory {
    FRUITS,
    VEGETABLES,
    DAIRY,
    MEAT,
    SEAFOOD,
    GRAINS,
    BAKERY,
    BEVERAGES,
    SNACKS,
    CONDIMENTS,
    FROZEN,
    CANNED,
    OTHER;
    
    companion object {
        fun fromString(value: String): ProductCategory {
            return entries.find { it.name == value.uppercase() } ?: OTHER
        }
    }
}

/**
 * Nutrition information
 */
@Serializable
data class NutritionInfo(
    val servingSize: String,
    val calories: Int,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val fiberG: Double?,
    val sugarG: Double?,
    val sodiumMg: Double?
)

/**
 * Domain model for PantryItem
 */
data class PantryItem(
    val id: String,
    val userId: String,
    val product: Product,
    val quantity: Int,
    val unit: MeasurementUnit,
    val location: StorageLocation,
    val purchaseDate: Long,
    val expirationDate: Long,
    val notes: String?,
    val status: ItemStatus
)

/**
 * Storage locations
 */
enum class StorageLocation {
    FRIDGE,
    PANTRY,
    FREEZER;
    
    companion object {
        fun fromString(value: String): StorageLocation {
            return entries.find { it.name == value.uppercase() } ?: PANTRY
        }
    }
}

/**
 * Measurement units
 */
enum class MeasurementUnit(val displayName: String) {
    UNITS("units"),
    KILOGRAMS("kg"),
    GRAMS("g"),
    LITERS("L"),
    MILLILITERS("mL"),
    POUNDS("lb"),
    OUNCES("oz");
    
    companion object {
        fun fromString(value: String): MeasurementUnit {
            return entries.find { it.name.lowercase() == value.lowercase() } ?: UNITS
        }
    }
}

/**
 * Item status based on expiration
 */
enum class ItemStatus {
    FRESH,
    EXPIRING_SOON,
    EXPIRED;
    
    companion object {
        fun fromExpirationDate(expirationDate: Long): ItemStatus {
            val now = System.currentTimeMillis()
            val daysUntilExpiration = (expirationDate - now) / (24 * 60 * 60 * 1000)
            
            return when {
                daysUntilExpiration < 0 -> EXPIRED
                daysUntilExpiration <= 3 -> EXPIRING_SOON
                else -> FRESH
            }
        }
    }
}

/**
 * Domain model for NutritionLog
 */
data class NutritionLog(
    val id: String,
    val userId: String,
    val mealDate: Long,
    val mealType: MealType,
    val calories: Int,
    val proteinG: Double,
    val carbsG: Double,
    val fatG: Double,
    val imageUrl: String?,
    val notes: String?
)

/**
 * Meal types
 */
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK;
    
    companion object {
        fun fromString(value: String): MealType {
            return entries.find { it.name == value.uppercase() } ?: SNACK
        }
    }
}

/**
 * Daily nutrition totals
 */
data class DailyNutrition(
    val date: Long,
    val totalCalories: Int,
    val totalProtein: Double,
    val totalCarbs: Double,
    val totalFat: Double,
    val meals: List<NutritionLog>
)

/**
 * Domain model for Receipt
 */
data class Receipt(
    val id: String,
    val userId: String,
    val imageUrl: String,
    val scanDate: Long,
    val processed: Boolean,
    val extractedItems: List<ReceiptItem>?
)

/**
 * Item extracted from receipt
 */
@Serializable
data class ReceiptItem(
    val name: String,
    val quantity: Int,
    val price: Double?,
    val category: ProductCategory?
)
