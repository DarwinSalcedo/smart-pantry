package com.smart.pantry.core.database

import app.cash.sqldelight.db.SqlDriver
import com.smart.pantry.database.SmartPantryDatabase

/**
 * Database configuration and initialization
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

/**
 * Database provider - Singleton instance
 */
object DatabaseProvider {
    
    private var database: SmartPantryDatabase? = null
    
    fun provideDatabase(driverFactory: DatabaseDriverFactory): SmartPantryDatabase {
        return database ?: SmartPantryDatabase(driverFactory.createDriver()).also {
            database = it
        }
    }
    
    fun getDatabase(): SmartPantryDatabase {
        return database ?: throw IllegalStateException("Database not initialized. Call provideDatabase() first.")
    }
}
