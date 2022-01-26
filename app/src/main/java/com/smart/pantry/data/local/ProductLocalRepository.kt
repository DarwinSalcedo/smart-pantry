package com.smart.pantry.data.local

import com.smart.pantry.data.ProductDataSource
import com.smart.pantry.data.dto.*
import kotlinx.coroutines.*


class ProductLocalRepository(
    private val productDao: ProductDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductDataSource {


    override suspend fun getProducts() : Result<List<Product>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(productDao.getProducts())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
    }

    override suspend fun saveProduct(product : Product) =
        withContext(ioDispatcher) {
            productDao.saveProduct(product)
        }

    override suspend fun getProductById(id: String): Result<Product> = withContext(ioDispatcher) {
            try {
                val shoppingList = productDao.getProductById(id)
                if (shoppingList != null) {
                    return@withContext Result.Success(shoppingList)
                } else {
                    return@withContext Result.Error("Product List not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
    }


    override suspend fun deleteAllProducts() {
        withContext(ioDispatcher) {
                productDao.deleteAllProducts()
        }
    }
}
