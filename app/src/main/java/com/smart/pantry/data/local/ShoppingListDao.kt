package com.smart.pantry.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smart.pantry.data.dto.ShoppingList

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list ORDER BY creation_date DESC ")
    fun getShoppingList(): List<ShoppingList>


    @Query("SELECT * FROM shopping_list where shopping_list_id = :id")
    fun getShoppingListById(id: String): ShoppingList?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveShoppingList(shoppingList: ShoppingList)


    @Query("DELETE FROM shopping_list")
    fun deleteAllShoppingList()

}