package com.smart.pantry.core.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.smart.pantry.database.SmartPantryDatabase

/**
 * iOS-specific database driver factory
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = SmartPantryDatabase.Schema,
            name = "smartpantry.db"
        )
    }
}
