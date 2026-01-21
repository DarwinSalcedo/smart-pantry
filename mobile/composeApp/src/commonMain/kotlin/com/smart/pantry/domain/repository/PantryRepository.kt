package com.smart.pantry.domain.repository

import com.smart.pantry.domain.model.PantryItem
import com.smart.pantry.domain.model.StorageLocation
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for PantryItem operations
 * Abstracts data layer from domain layer
 */
interface PantryRepository {
    
    /**
     * Get all pantry items for current user
     */
    fun getAllItems(): Flow<List<PantryItem>>
    
    /**
     * Get items by storage location
     */
    fun getItemsByLocation(location: StorageLocation): Flow<List<PantryItem>>
    
    /**
     * Get expiring items (within specified days)
     */
    fun getExpiringItems(withinDays: Int): Flow<List<PantryItem>>
    
    /**
     * Get expired items
     */
    fun getExpiredItems(): Flow<List<PantryItem>>
    
    /**
     * Get item by ID
     */
    suspend fun getItemById(id: String): PantryItem?
    
    /**
     * Add new item to pantry
     */
    suspend fun addItem(item: PantryItem): Result<Unit>
    
    /**
     * Update existing item
     */
    suspend fun updateItem(item: PantryItem): Result<Unit>
    
    /**
     * Update item quantity
     */
    suspend fun updateQuantity(itemId: String, newQuantity: Int): Result<Unit>
    
    /**
     * Delete item
     */
    suspend fun deleteItem(itemId: String): Result<Unit>
    
    /**
     * Sync pending changes with backend
     */
    suspend fun syncWithBackend(): Result<Unit>
}
