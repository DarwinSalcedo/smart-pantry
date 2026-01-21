package com.smart.pantry.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.smart.pantry.database.SmartPantryDatabase
import com.smart.pantry.data.local.entity.PantryItemEntity
import com.smart.pantry.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for PantryItem operations
 */
class PantryItemDao(private val database: SmartPantryDatabase) {
    
    private val queries = database.smartPantryQueries
    
    /**
     * Get all pantry items for a user
     */
    fun getAllPantryItems(userId: String): Flow<List<PantryItemEntity>> {
        return queries.selectAllPantryItems(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { items ->
                items.map { it.toEntity() }
            }
    }
    
    /**
     * Get pantry items by location
     */
    fun getPantryItemsByLocation(userId: String, location: String): Flow<List<PantryItemEntity>> {
        return queries.selectPantryItemsByLocation(userId, location)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { items ->
                items.map { it.toEntity() }
            }
    }
    
    /**
     * Get expiring items (before given timestamp)
     */
    fun getExpiringItems(userId: String, beforeTimestamp: Long): Flow<List<PantryItemEntity>> {
        return queries.selectExpiringItems(userId, beforeTimestamp)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { items ->
                items.map { it.toEntity() }
            }
    }
    
    /**
     * Get item by ID
     */
    suspend fun getPantryItemById(id: String): PantryItemEntity? {
        return queries.selectPantryItemById(id)
            .executeAsOneOrNull()
            ?.toEntity()
    }
    
    /**
     * Insert or update pantry item
     */
    suspend fun insertPantryItem(item: PantryItemEntity) {
        queries.insertPantryItem(
            id = item.id,
            userId = item.userId,
            productId = item.productId,
            productName = item.productName,
            quantity = item.quantity.toLong(),
            unit = item.unit,
            location = item.location,
            purchaseDate = item.purchaseDate,
            expirationDate = item.expirationDate,
            notes = item.notes,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt,
            syncStatus = item.syncStatus
        )
    }
    
    /**
     * Update quantity
     */
    suspend fun updateQuantity(id: String, quantity: Int, updatedAt: Long) {
        queries.updatePantryItemQuantity(
            quantity = quantity.toLong(),
            updatedAt = updatedAt,
            id = id
        )
    }
    
    /**
     * Update sync status
     */
    suspend fun updateSyncStatus(id: String, syncStatus: String) {
        queries.updatePantryItemSyncStatus(syncStatus, id)
    }
    
    /**
     * Delete pantry item
     */
    suspend fun deletePantryItem(id: String) {
        queries.deletePantryItem(id)
    }
    
    /**
     * Get pending sync items
     */
    suspend fun getPendingSyncItems(): List<PantryItemEntity> {
        return queries.selectPendingSyncItems()
            .executeAsList()
            .map { it.toEntity() }
    }
}
