package com.smart.pantry.data.local

import androidx.room.*
import com.smart.pantry.data.dto.Item
import com.smart.pantry.data.dto.ProductAndItem
import com.smart.pantry.data.dto.ShoppingListWithItem

@Dao
interface ItemDao {

    @Query("SELECT * FROM item ")
    fun getItems(): List<Item>

    @Query("SELECT * FROM item where item_id = :id")
    fun getItemById(id: String): Item?

    @Transaction
    @Query("SELECT * FROM product join item where product_id = product_creator_id")
    fun getProductsAndItems(): List<ProductAndItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveItem(item: Item)

    @Transaction
    @Query("SELECT * FROM shopping_list join item  where  shopping_list_id = shopping_list_creator_id and shopping_list_id = :shoppingListId")
    fun getProductsForShoppingList(shoppingListId: String): ShoppingListWithItem

    @Transaction
    @Query("SELECT * FROM product join item where product_id = product_creator_id and  product_id IN (:productIds) and shopping_list_creator_id = :shoppingListId")
    fun getProductsAndItems(productIds: List<String>, shoppingListId: String): List<ProductAndItem>


}