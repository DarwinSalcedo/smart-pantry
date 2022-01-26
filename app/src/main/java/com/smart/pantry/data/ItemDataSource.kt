package com.smart.pantry.data

import com.smart.pantry.data.dto.*

interface ItemDataSource {
    suspend fun saveItem(item: Item)
    suspend fun getItems(): Result<List<Item>>
    suspend fun getItemById(id:String): Result<Item>
    suspend fun getItemForProducts(id: String): Result<List<ProductAndItem>>
    suspend fun getProductsForShoppingList(id: String): Result<ShoppingListWithProduct>
}