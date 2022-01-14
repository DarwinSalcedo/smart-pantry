package com.smart.pantry.data.local

import com.smart.pantry.data.dto.ShoppingList
import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.ShoppingListDataSource
import kotlinx.coroutines.*


class ShoppingListLocalRepository(
    private val shoppingListDao: ShoppingListDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ShoppingListDataSource {


    override suspend fun getShoppingList(): Result<List<ShoppingList>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(shoppingListDao.getShoppingList())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
    }

    override suspend fun saveShoppingList(shoppingList: ShoppingList) =
        withContext(ioDispatcher) {
                shoppingListDao.saveShoppingList(shoppingList)
        }


    override suspend fun getShoppingList(id: String): Result<ShoppingList> = withContext(ioDispatcher) {
            try {
                val shoppingList = shoppingListDao.getShoppingListById(id)
                if (shoppingList != null) {
                    return@withContext Result.Success(shoppingList)
                } else {
                    return@withContext Result.Error("Shopping List not found!")
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e.localizedMessage)
            }
    }


    override suspend fun deleteAllShoppingList() {
        withContext(ioDispatcher) {
                shoppingListDao.deleteAllShoppingList()
        }
    }
}
