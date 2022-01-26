package com.smart.pantry.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.smart.pantry.data.dto.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber


object LocalDB {
    const val DATABASE_NAME = "local_pantry.db"
    @Volatile
    private var INSTANCE: PantryDatabase? = null

    private fun getDatabase(context: Context): PantryDatabase {
        val tempInstance = INSTANCE
        if (tempInstance != null) {
            return tempInstance
        }
        synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                PantryDatabase::class.java, DATABASE_NAME
            ).addCallback(DatabaseCallback()).build()
            INSTANCE = instance
            return instance
        }
    }

    fun createShoppingListDao(context: Context): ShoppingListDao {
        return getDatabase(context).shoppingListDao()
    }

    fun createProductDao(context: Context): ProductDao {
        return getDatabase(context).productDao()
    }

    fun createItemDao(context: Context): ItemDao
    {
        return getDatabase(context).itemDao()
    }
    private class DatabaseCallback(
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                GlobalScope.launch {
                    populateDatabase(
                        database.productDao()
                    )
                }
            }
        }
    }

    fun populateDatabase(
        dao: ProductDao
    ) {
        Timber.e("populateDatabase")
        val p = Product(name = "Lemon", code = "AAA")
        val p1 = Product(name = "Apple", code = "AAA1")
        val p2 = Product(name = "Rice", code = "AAA2")
        val p3 = Product(name = "Water", code = "AAA2")
        dao.saveProduct(p)
        dao.saveProduct(p1)
        dao.saveProduct(p2)
        dao.saveProduct(p3)
    }
}







