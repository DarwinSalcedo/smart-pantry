package com.smart.pantry.core.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.smart.pantry.database.SmartPantryDatabase

/**
 * Android-specific database driver factory
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = SmartPantryDatabase.Schema,
            context = context,
            name = "smartpantry.db"
        )
    }
}
