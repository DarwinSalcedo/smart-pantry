package com.smart.pantry.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.smart.pantry.database.SmartPantryDatabase
import com.smart.pantry.data.local.entity.ProductEntity
import com.smart.pantry.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for Product operations
 */
class ProductDao(private val database: SmartPantryDatabase) {
    
    private val queries = database.smartPantryQueries
    
    /**
     * Get all products
     */
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return queries.selectAllProducts()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { products ->
                products.map { it.toEntity() }
            }
    }
    
    /**
     * Get products by category
     */
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> {
        return queries.selectProductsByCategory(category)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { products ->
                products.map { it.toEntity() }
            }
    }
    
    /**
     * Get product by ID
     */
    suspend fun getProductById(id: String): ProductEntity? {
        return queries.selectProductById(id)
            .executeAsOneOrNull()
            ?.toEntity()
    }
    
    /**
     * Get product by barcode
     */
    suspend fun getProductByBarcode(barcode: String): ProductEntity? {
        return queries.selectProductByBarcode(barcode)
            .executeAsOneOrNull()
            ?.toEntity()
    }
    
    /**
     * Insert or update product
     */
    suspend fun insertProduct(product: ProductEntity) {
        queries.insertProduct(
            id = product.id,
            name = product.name,
            category = product.category,
            defaultShelfLifeDays = product.defaultShelfLifeDays.toLong(),
            barcode = product.barcode,
            imageUrl = product.imageUrl,
            nutritionInfo = product.nutritionInfo,
            createdAt = product.createdAt
        )
    }
    
    /**
     * Insert multiple products (batch)
     */
    suspend fun insertProducts(products: List<ProductEntity>) {
        queries.transaction {
            products.forEach { product ->
                queries.insertProduct(
                    id = product.id,
                    name = product.name,
                    category = product.category,
                    defaultShelfLifeDays = product.defaultShelfLifeDays.toLong(),
                    barcode = product.barcode,
                    imageUrl = product.imageUrl,
                    nutritionInfo = product.nutritionInfo,
                    createdAt = product.createdAt
                )
            }
        }
    }
    
    /**
     * Delete product
     */
    suspend fun deleteProduct(id: String) {
        queries.deleteProduct(id)
    }
}
