package com.smart.pantry.di

import com.smart.pantry.data.local.dao.NutritionLogDao
import com.smart.pantry.data.local.dao.PantryItemDao
import com.smart.pantry.data.local.dao.PendingSyncDao
import com.smart.pantry.data.local.dao.ProductDao
import org.koin.dsl.module

/**
 * Koin module for database and DAOs
 */
val databaseModule = module {
    
    // DAOs
    single { PantryItemDao(get()) }
    single { ProductDao(get()) }
    single { NutritionLogDao(get()) }
    single { PendingSyncDao(get()) }
}

/**
 * Platform-specific database module
 * Should be provided by each platform (Android/iOS)
 */
expect val platformDatabaseModule: org.koin.core.module.Module
