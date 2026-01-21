package com.smart.pantry.di

import com.smart.pantry.core.database.DatabaseDriverFactory
import com.smart.pantry.core.database.DatabaseProvider
import org.koin.dsl.module

/**
 * iOS-specific database module
 */
actual val platformDatabaseModule = module {
    
    // Database Driver Factory (no context needed for iOS)
    single { DatabaseDriverFactory() }
    
    // Database instance
    single {
        val driverFactory: DatabaseDriverFactory = get()
        DatabaseProvider.provideDatabase(driverFactory)
    }
}
