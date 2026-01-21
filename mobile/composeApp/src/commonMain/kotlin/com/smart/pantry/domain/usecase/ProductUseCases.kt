package com.smart.pantry.domain.usecase

import com.smart.pantry.domain.model.Product
import com.smart.pantry.domain.model.ProductCategory
import com.smart.pantry.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to search products
 */
class SearchProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return repository.searchProducts(query)
    }
}

/**
 * Use case to get products by category
 */
class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(category: ProductCategory): Flow<List<Product>> {
        return repository.getProductsByCategory(category)
    }
}

/**
 * Use case to scan barcode and get product
 */
class ScanBarcodeUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(barcode: String): Product? {
        return repository.getProductByBarcode(barcode)
    }
}
