package com.smart.pantry.di

import com.smart.pantry.presentation.nutrition.NutritionViewModel
import com.smart.pantry.presentation.pantry.PantryViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for ViewModels
 */
val viewModelModule = module {
    
    viewModel {
        PantryViewModel(
            getPantryItemsByLocation = get(),
            getExpiringItems = get(),
            getExpiredItems = get(),
            addPantryItem = get(),
            updateItemQuantity = get(),
            deletePantryItem = get()
        )
    }
    
    viewModel {
        NutritionViewModel(
            getNutritionLogsByDate = get(),
            getDailyNutritionTotals = get(),
            addNutritionLog = get()
        )
    }
}
