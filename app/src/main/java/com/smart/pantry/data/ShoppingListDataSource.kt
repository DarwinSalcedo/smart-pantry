package com.smart.pantry.data

import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.ShoppingList

interface ShoppingListDataSource {
    suspend fun getShoppingList(): Result<List<ShoppingList>>
    suspend fun saveShoppingList(shoppingList: ShoppingList)
    suspend fun getShoppingList(id: String): Result<ShoppingList>
    suspend fun deleteAllShoppingList()
}