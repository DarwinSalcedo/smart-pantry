package com.smart.pantry.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.smart.pantry.database.SmartPantryDatabase
import com.smart.pantry.data.local.entity.PendingSyncEntity
import com.smart.pantry.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for PendingSync operations (offline queue)
 */
class PendingSyncDao(private val database: SmartPantryDatabase) {
    
    private val queries = database.smartPantryQueries
    
    /**
     * Get all pending sync operations
     */
    fun getAllPendingSync(): Flow<List<PendingSyncEntity>> {
        return queries.selectAllPendingSync()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { items ->
                items.map { item -> item.toEntity() }
            }
    }
    
    /**
     * Get pending sync by entity
     */
    suspend fun getPendingSyncByEntity(entityType: String, entityId: String): PendingSyncEntity? {
        return queries.selectPendingSyncByEntity(entityType, entityId)
            .executeAsOneOrNull()
            ?.toEntity()
    }
    
    /**
     * Insert pending sync operation
     */
    suspend fun insertPendingSync(sync: PendingSyncEntity) {
        queries.insertPendingSync(
            id = sync.id,
            entityType = sync.entityType,
            entityId = sync.entityId,
            operation = sync.operation,
            payload = sync.payload,
            createdAt = sync.createdAt,
            retryCount = sync.retryCount.toLong(),
            lastError = sync.lastError
        )
    }
    
    /**
     * Update retry count and error
     */
    suspend fun updateRetry(id: String, error: String) {
        queries.updatePendingSyncRetry(error, id)
    }
    
    /**
     * Delete pending sync operation
     */
    suspend fun deletePendingSync(id: String) {
        queries.deletePendingSync(id)
    }
    
    /**
     * Clear all pending sync operations
     */
    suspend fun deleteAllPendingSync() {
        queries.deleteAllPendingSync()
    }
}
