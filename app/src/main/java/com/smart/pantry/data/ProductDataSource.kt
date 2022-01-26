package com.smart.pantry.data

import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.Product

interface ProductDataSource {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun getProductById(id: String): Result<Product>
    suspend fun saveProduct(Product: Product)
    suspend fun deleteAllProducts()
}