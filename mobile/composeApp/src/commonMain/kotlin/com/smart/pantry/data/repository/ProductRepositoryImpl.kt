package com.smart.pantry.data.repository

import com.smart.pantry.data.local.dao.ProductDao
import com.smart.pantry.data.mapper.toDomain
import com.smart.pantry.data.mapper.toEntity
import com.smart.pantry.domain.model.Product
import com.smart.pantry.domain.model.ProductCategory
import com.smart.pantry.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of ProductRepository
 * Handles product catalog operations
 */
class ProductRepositoryImpl(
    private val productDao: ProductDao
) : ProductRepository {
    
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getProductsByCategory(category: ProductCategory): Flow<List<Product>> {
        return productDao.getProductsByCategory(category.name)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.getAllProducts()
            .map { entities ->
                entities.filter { it.name.contains(query, ignoreCase = true) }
                    .map { it.toDomain() }
            }
    }
    
    override suspend fun getProductById(id: String): Product? {
        return productDao.getProductById(id)?.toDomain()
    }
    
    override suspend fun getProductByBarcode(barcode: String): Product? {
        return productDao.getProductByBarcode(barcode)?.toDomain()
    }
    
    override suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val entity = product.toEntity(createdAt = System.currentTimeMillis())
            productDao.insertProduct(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            val existing = productDao.getProductById(product.id)
            val entity = product.toEntity(
                createdAt = existing?.createdAt ?: System.currentTimeMillis()
            )
            productDao.insertProduct(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            productDao.deleteProduct(productId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun syncCatalog(): Result<Unit> {
        return try {
            // TODO: Implement backend sync
            // 1. Fetch products from backend
            // 2. Update local database
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
