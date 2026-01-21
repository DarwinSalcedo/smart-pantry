package com.smart.pantry.domain.repository

import com.smart.pantry.domain.model.Product
import com.smart.pantry.domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Product catalog operations
 */
interface ProductRepository {
    
    /**
     * Get all products
     */
    fun getAllProducts(): Flow<List<Product>>
    
    /**
     * Get products by category
     */
    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>>
    
    /**
     * Search products by name
     */
    fun searchProducts(query: String): Flow<List<Product>>
    
    /**
     * Get product by ID
     */
    suspend fun getProductById(id: String): Product?
    
    /**
     * Get product by barcode
     */
    suspend fun getProductByBarcode(barcode: String): Product?
    
    /**
     * Add new product to catalog
     */
    suspend fun addProduct(product: Product): Result<Unit>
    
    /**
     * Update product information
     */
    suspend fun updateProduct(product: Product): Result<Unit>
    
    /**
     * Delete product
     */
    suspend fun deleteProduct(productId: String): Result<Unit>
    
    /**
     * Sync product catalog from backend
     */
    suspend fun syncCatalog(): Result<Unit>
}
