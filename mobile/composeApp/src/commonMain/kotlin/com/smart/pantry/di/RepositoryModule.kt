package com.smart.pantry.di

import com.smart.pantry.data.repository.NutritionRepositoryImpl
import com.smart.pantry.data.repository.PantryRepositoryImpl
import com.smart.pantry.data.repository.ProductRepositoryImpl
import com.smart.pantry.domain.repository.NutritionRepository
import com.smart.pantry.domain.repository.PantryRepository
import com.smart.pantry.domain.repository.ProductRepository
import org.koin.dsl.module

/**
 * Koin module for repositories
 */
val repositoryModule = module {
    
    // Current user ID provider (placeholder - will be replaced with actual auth)
    single<() -> String> { { "current-user-id" } }
    
    // Repositories
    single<PantryRepository> {
        PantryRepositoryImpl(
            pantryItemDao = get(),
            productDao = get(),
            currentUserId = get()
        )
    }
    
    single<ProductRepository> {
        ProductRepositoryImpl(
            productDao = get()
        )
    }
    
    single<NutritionRepository> {
        NutritionRepositoryImpl(
            nutritionLogDao = get(),
            currentUserId = get()
        )
    }
}
