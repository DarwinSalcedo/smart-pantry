package com.smart.pantry.domain.usecase

import com.smart.pantry.domain.model.DailyNutrition
import com.smart.pantry.domain.model.NutritionLog
import com.smart.pantry.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get nutrition logs by date
 */
class GetNutritionLogsByDateUseCase(
    private val repository: NutritionRepository
) {
    operator fun invoke(date: Long): Flow<List<NutritionLog>> {
        return repository.getLogsByDate(date)
    }
}

/**
 * Use case to get daily nutrition totals
 */
class GetDailyNutritionTotalsUseCase(
    private val repository: NutritionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<List<DailyNutrition>> {
        return repository.getDailyTotals(startDate, endDate)
    }
}

/**
 * Use case to add nutrition log
 */
class AddNutritionLogUseCase(
    private val repository: NutritionRepository
) {
    suspend operator fun invoke(log: NutritionLog): Result<Unit> {
        // Validate nutrition values
        if (log.calories < 0 || log.proteinG < 0 || log.carbsG < 0 || log.fatG < 0) {
            return Result.failure(IllegalArgumentException("Nutrition values cannot be negative"))
        }
        return repository.addLog(log)
    }
}
