package com.smart.pantry.data.local

import android.content.Context
import androidx.room.Room


object LocalDB {


    fun createShoppingListDao(context: Context): ShoppingListDao {
        return Room.databaseBuilder(
            context.applicationContext,
            PantryDatabase::class.java, "local_pantry.db"
        ).build().shoppingListDao()
    }

}