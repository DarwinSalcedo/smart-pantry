package com.smart.pantry.domain.usecase

import com.smart.pantry.domain.model.PantryItem
import com.smart.pantry.domain.model.StorageLocation
import com.smart.pantry.domain.repository.PantryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get all pantry items by location
 */
class GetPantryItemsByLocationUseCase(
    private val repository: PantryRepository
) {
    operator fun invoke(location: StorageLocation): Flow<List<PantryItem>> {
        return repository.getItemsByLocation(location)
    }
}

/**
 * Use case to get expiring items
 */
class GetExpiringItemsUseCase(
    private val repository: PantryRepository
) {
    operator fun invoke(withinDays: Int = 3): Flow<List<PantryItem>> {
        return repository.getExpiringItems(withinDays)
    }
}

/**
 * Use case to get expired items
 */
class GetExpiredItemsUseCase(
    private val repository: PantryRepository
) {
    operator fun invoke(): Flow<List<PantryItem>> {
        return repository.getExpiredItems()
    }
}

/**
 * Use case to add item to pantry
 */
class AddPantryItemUseCase(
    private val repository: PantryRepository
) {
    suspend operator fun invoke(item: PantryItem): Result<Unit> {
        return repository.addItem(item)
    }
}

/**
 * Use case to update item quantity
 */
class UpdateItemQuantityUseCase(
    private val repository: PantryRepository
) {
    suspend operator fun invoke(itemId: String, newQuantity: Int): Result<Unit> {
        if (newQuantity < 0) {
            return Result.failure(IllegalArgumentException("Quantity cannot be negative"))
        }
        return repository.updateQuantity(itemId, newQuantity)
    }
}

/**
 * Use case to delete item
 */
class DeletePantryItemUseCase(
    private val repository: PantryRepository
) {
    suspend operator fun invoke(itemId: String): Result<Unit> {
        return repository.deleteItem(itemId)
    }
}
