package com.smart.pantry.data.local.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.smart.pantry.database.SmartPantryDatabase
import com.smart.pantry.data.local.entity.NutritionLogEntity
import com.smart.pantry.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DAO for NutritionLog operations
 */
class NutritionLogDao(private val database: SmartPantryDatabase) {
    
    private val queries = database.smartPantryQueries
    
    /**
     * Get all nutrition logs for a user
     */
    fun getAllNutritionLogs(userId: String): Flow<List<NutritionLogEntity>> {
        return queries.selectAllNutritionLogs(userId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { logs ->
                logs.map { it.toEntity() }
            }
    }
    
    /**
     * Get nutrition logs by date
     */
    fun getNutritionLogsByDate(userId: String, date: Long): Flow<List<NutritionLogEntity>> {
        return queries.selectNutritionLogsByDate(userId, date)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { logs ->
                logs.map { it.toEntity() }
            }
    }
    
    /**
     * Get nutrition logs by date range
     */
    fun getNutritionLogsByDateRange(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<NutritionLogEntity>> {
        return queries.selectNutritionLogsByDateRange(userId, startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { logs ->
                logs.map { it.toEntity() }
            }
    }
    
    /**
     * Get daily totals for date range
     */
    data class DailyTotals(
        val mealDate: Long,
        val totalCalories: Long,
        val totalProtein: Double,
        val totalCarbs: Double,
        val totalFat: Double
    )
    
    fun getDailyTotals(
        userId: String,
        startDate: Long,
        endDate: Long
    ): Flow<List<DailyTotals>> {
        return queries.selectDailyTotals(userId, startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { totals ->
                totals.map {
                    DailyTotals(
                        mealDate = it.mealDate,
                        totalCalories = it.totalCalories ?: 0,
                        totalProtein = it.totalProtein ?: 0.0,
                        totalCarbs = it.totalCarbs ?: 0.0,
                        totalFat = it.totalFat ?: 0.0
                    )
                }
            }
    }
    
    /**
     * Get log by ID
     */
    suspend fun getNutritionLogById(id: String): NutritionLogEntity? {
        return queries.selectNutritionLogById(id)
            .executeAsOneOrNull()
            ?.toEntity()
    }
    
    /**
     * Insert or update nutrition log
     */
    suspend fun insertNutritionLog(log: NutritionLogEntity) {
        queries.insertNutritionLog(
            id = log.id,
            userId = log.userId,
            mealDate = log.mealDate,
            mealType = log.mealType,
            calories = log.calories.toLong(),
            proteinG = log.proteinG,
            carbsG = log.carbsG,
            fatG = log.fatG,
            imageUrl = log.imageUrl,
            notes = log.notes,
            createdAt = log.createdAt,
            syncStatus = log.syncStatus
        )
    }
    
    /**
     * Update sync status
     */
    suspend fun updateSyncStatus(id: String, syncStatus: String) {
        queries.updateNutritionLogSyncStatus(syncStatus, id)
    }
    
    /**
     * Delete nutrition log
     */
    suspend fun deleteNutritionLog(id: String) {
        queries.deleteNutritionLog(id)
    }
}

