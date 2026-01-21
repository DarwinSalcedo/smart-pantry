package com.smart.pantry.data.repository

import com.smart.pantry.data.local.dao.PantryItemDao
import com.smart.pantry.data.local.dao.ProductDao
import com.smart.pantry.data.mapper.toDomain
import com.smart.pantry.data.mapper.toEntity
import com.smart.pantry.domain.model.PantryItem
import com.smart.pantry.domain.model.StorageLocation
import com.smart.pantry.domain.repository.PantryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of PantryRepository
 * Handles data operations for pantry items
 */
class PantryRepositoryImpl(
    private val pantryItemDao: PantryItemDao,
    private val productDao: ProductDao,
    private val currentUserId: () -> String
) : PantryRepository {
    
    override fun getAllItems(): Flow<List<PantryItem>> {
        return pantryItemDao.getAllPantryItems(currentUserId())
            .map { entities ->
                entities.mapNotNull { entity ->
                    val product = productDao.getProductById(entity.productId)
                        ?.toDomain()
                    product?.let { entity.toDomain(it) }
                }
            }
    }
    
    override fun getItemsByLocation(location: StorageLocation): Flow<List<PantryItem>> {
        return pantryItemDao.getPantryItemsByLocation(currentUserId(), location.name)
            .map { entities ->
                entities.mapNotNull { entity ->
                    val product = productDao.getProductById(entity.productId)
                        ?.toDomain()
                    product?.let { entity.toDomain(it) }
                }
            }
    }
    
    override fun getExpiringItems(withinDays: Int): Flow<List<PantryItem>> {
        val expirationThreshold = System.currentTimeMillis() + (withinDays * 24 * 60 * 60 * 1000)
        return pantryItemDao.getExpiringItems(currentUserId(), expirationThreshold)
            .map { entities ->
                entities.mapNotNull { entity ->
                    val product = productDao.getProductById(entity.productId)
                        ?.toDomain()
                    product?.let { entity.toDomain(it) }
                }
            }
    }
    
    override fun getExpiredItems(): Flow<List<PantryItem>> {
        val now = System.currentTimeMillis()
        return pantryItemDao.getExpiringItems(currentUserId(), now)
            .map { entities ->
                entities.filter { it.expirationDate < now }
                    .mapNotNull { entity ->
                        val product = productDao.getProductById(entity.productId)
                            ?.toDomain()
                        product?.let { entity.toDomain(it) }
                    }
            }
    }
    
    override suspend fun getItemById(id: String): PantryItem? {
        val entity = pantryItemDao.getPantryItemById(id) ?: return null
        val product = productDao.getProductById(entity.productId)
            ?.toDomain() ?: return null
        return entity.toDomain(product)
    }
    
    override suspend fun addItem(item: PantryItem): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            
            // First, save the product if it doesn't exist
            val existingProduct = productDao.getProductById(item.product.id)
            if (existingProduct == null) {
                val productEntity = item.product.toEntity(createdAt = now)
                productDao.insertProduct(productEntity)
            }
            
            // Then save the pantry item
            val entity = item.toEntity(
                createdAt = now,
                updatedAt = now,
                syncStatus = "PENDING"
            )
            pantryItemDao.insertPantryItem(entity)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateItem(item: PantryItem): Result<Unit> {
        return try {
            val now = System.currentTimeMillis()
            
            // First, ensure the product exists
            val existingProduct = productDao.getProductById(item.product.id)
            if (existingProduct == null) {
                val productEntity = item.product.toEntity(createdAt = now)
                productDao.insertProduct(productEntity)
            }
            
            // Then update the pantry item
            val existing = pantryItemDao.getPantryItemById(item.id)
            val entity = item.toEntity(
                createdAt = existing?.createdAt ?: now,
                updatedAt = now,
                syncStatus = "PENDING"
            )
            pantryItemDao.insertPantryItem(entity)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateQuantity(itemId: String, newQuantity: Int): Result<Unit> {
        return try {
            pantryItemDao.updateQuantity(itemId, newQuantity, System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteItem(itemId: String): Result<Unit> {
        return try {
            pantryItemDao.deletePantryItem(itemId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncWithBackend(): Result<Unit> {
        return try {
            // TODO: Implement backend sync logic
            // 1. Get pending sync items
            // 2. Send to backend
            // 3. Update sync status
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
