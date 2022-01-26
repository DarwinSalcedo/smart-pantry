package com.smart.pantry.data.local

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.smart.pantry.data.ItemDataSource
import com.smart.pantry.data.dto.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class ItemLocalRepository(
    private val itemDao: ItemDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ItemDataSource {


    override suspend fun getItems() : Result<List<Item>> = withContext(ioDispatcher) {
            return@withContext try {
                Result.Success(itemDao.getItems())
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
    }

    override suspend fun getItemById(id : String) : Result<Item> = withContext(ioDispatcher) {
            return@withContext try {
                val data = itemDao.getItemById(id)
                if (data != null) {
                    return@withContext Result.Success(data)
                } else {
                    return@withContext Result.Error("Item not found!")
                }
            } catch (ex: Exception) {
                Result.Error(ex.localizedMessage)
            }
    }

    override suspend fun getItemForProducts(id: String): Result<List<ProductAndItem>>  = withContext(ioDispatcher) {
        return@withContext try {
            val data = itemDao.getProductsAndItems()
            if (data != null) {
                return@withContext Result.Success(data)
            } else {
                return@withContext Result.Error("Item not found!")
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }


    override suspend fun getProductsForShoppingList(id: String): Result<ShoppingListWithProduct> = withContext(ioDispatcher) {
        return@withContext try {
            val result = itemDao.getProductsForShoppingList(id)
            if (result != null) {
                val dataList = ArrayList<String>()
                dataList.addAll((result.products).map { data ->
                 data.productCreatorId
                })

                val productsAndItem = itemDao.getProductsAndItems(dataList,id)

                return@withContext Result.Success(ShoppingListWithProduct(result.shoppingList,productsAndItem))
            } else {
                return@withContext Result.Error("Item not found!")
            }
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }
    override suspend fun saveItem(item : Item) =
        withContext(ioDispatcher) {
            itemDao.saveItem(item)
        }

}
