package com.smart.pantry.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smart.pantry.data.dto.ShoppingList

@Database(entities = [ShoppingList::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class PantryDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao
}