package com.smart.pantry.domain.repository

import com.smart.pantry.domain.model.DailyNutrition
import com.smart.pantry.domain.model.MealType
import com.smart.pantry.domain.model.NutritionLog
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Nutrition tracking operations
 */
interface NutritionRepository {
    
    /**
     * Get all nutrition logs
     */
    fun getAllLogs(): Flow<List<NutritionLog>>
    
    /**
     * Get logs for specific date
     */
    fun getLogsByDate(date: Long): Flow<List<NutritionLog>>
    
    /**
     * Get logs for date range
     */
    fun getLogsByDateRange(startDate: Long, endDate: Long): Flow<List<NutritionLog>>
    
    /**
     * Get daily nutrition totals for date range
     */
    fun getDailyTotals(startDate: Long, endDate: Long): Flow<List<DailyNutrition>>
    
    /**
     * Get log by ID
     */
    suspend fun getLogById(id: String): NutritionLog?
    
    /**
     * Add nutrition log
     */
    suspend fun addLog(log: NutritionLog): Result<Unit>
    
    /**
     * Update nutrition log
     */
    suspend fun updateLog(log: NutritionLog): Result<Unit>
    
    /**
     * Delete nutrition log
     */
    suspend fun deleteLog(logId: String): Result<Unit>
    
    /**
     * Get weekly average calories
     */
    suspend fun getWeeklyAverageCalories(): Int
    
    /**
     * Get weekly average macros
     */
    suspend fun getWeeklyAverageMacros(): Triple<Double, Double, Double> // protein, carbs, fat
}
