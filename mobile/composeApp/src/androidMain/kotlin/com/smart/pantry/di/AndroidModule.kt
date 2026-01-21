package com.smart.pantry.di

import android.content.Context
import com.smart.pantry.core.database.DatabaseDriverFactory
import com.smart.pantry.core.database.DatabaseProvider
import org.koin.dsl.module

/**
 * Android-specific database module
 */
actual val platformDatabaseModule = module {
    
    // Database Driver Factory
    single { 
        val context: Context = get()
        DatabaseDriverFactory(context)
    }
    
    // Database instance
    single {
        val driverFactory: DatabaseDriverFactory = get()
        DatabaseProvider.provideDatabase(driverFactory)
    }
}
