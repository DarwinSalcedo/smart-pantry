package com.smart.pantry.di

import com.smart.pantry.data.repository.NutritionRepositoryImpl
import com.smart.pantry.data.repository.PantryRepositoryImpl
import com.smart.pantry.data.repository.ProductRepositoryImpl
import com.smart.pantry.domain.repository.NutritionRepository
import com.smart.pantry.domain.repository.PantryRepository
import com.smart.pantry.domain.repository.ProductRepository
import com.smart.pantry.domain.usecase.*
import org.koin.dsl.module

/**
 * Koin module for repositories and use cases
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
    
    // Pantry Use Cases
    factory { GetPantryItemsByLocationUseCase(get()) }
    factory { GetExpiringItemsUseCase(get()) }
    factory { GetExpiredItemsUseCase(get()) }
    factory { AddPantryItemUseCase(get()) }
    factory { UpdateItemQuantityUseCase(get()) }
    factory { DeletePantryItemUseCase(get()) }
    
    // Product Use Cases
    factory { SearchProductsUseCase(get()) }
    factory { GetProductsByCategoryUseCase(get()) }
    factory { ScanBarcodeUseCase(get()) }
    
    // Nutrition Use Cases
    factory { GetNutritionLogsByDateUseCase(get()) }
    factory { GetDailyNutritionTotalsUseCase(get()) }
    factory { AddNutritionLogUseCase(get()) }
}
