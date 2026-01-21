package com.smart.pantry.data.repository

import com.smart.pantry.data.local.dao.NutritionLogDao
import com.smart.pantry.data.mapper.toDomain
import com.smart.pantry.data.mapper.toEntity
import com.smart.pantry.domain.model.DailyNutrition
import com.smart.pantry.domain.model.NutritionLog
import com.smart.pantry.domain.repository.NutritionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of NutritionRepository
 * Handles nutrition tracking and analytics
 */
class NutritionRepositoryImpl(
    private val nutritionLogDao: NutritionLogDao,
    private val currentUserId: () -> String
) : NutritionRepository {
    
    override fun getAllLogs(): Flow<List<NutritionLog>> {
        return nutritionLogDao.getAllNutritionLogs(currentUserId())
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getLogsByDate(date: Long): Flow<List<NutritionLog>> {
        return nutritionLogDao.getNutritionLogsByDate(currentUserId(), date)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getLogsByDateRange(startDate: Long, endDate: Long): Flow<List<NutritionLog>> {
        return nutritionLogDao.getNutritionLogsByDateRange(currentUserId(), startDate, endDate)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override fun getDailyTotals(startDate: Long, endDate: Long): Flow<List<DailyNutrition>> {
        return nutritionLogDao.getDailyTotals(currentUserId(), startDate, endDate)
            .map { totals ->
                totals.map { total ->
                    DailyNutrition(
                        date = total.mealDate,
                        totalCalories = total.totalCalories.toInt(),
                        totalProtein = total.totalProtein,
                        totalCarbs = total.totalCarbs,
                        totalFat = total.totalFat,
                        meals = emptyList() // TODO: Load meals for this date
                    )
                }
            }
    }
    
    override suspend fun getLogById(id: String): NutritionLog? {
        return nutritionLogDao.getNutritionLogById(id)?.toDomain()
    }
    
    override suspend fun addLog(log: NutritionLog): Result<Unit> {
        return try {
            val entity = log.toEntity(
                createdAt = System.currentTimeMillis(),
                syncStatus = "PENDING"
            )
            nutritionLogDao.insertNutritionLog(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateLog(log: NutritionLog): Result<Unit> {
        return try {
            val existing = nutritionLogDao.getNutritionLogById(log.id)
            val entity = log.toEntity(
                createdAt = existing?.createdAt ?: System.currentTimeMillis(),
                syncStatus = "PENDING"
            )
            nutritionLogDao.insertNutritionLog(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteLog(logId: String): Result<Unit> {
        return try {
            nutritionLogDao.deleteNutritionLog(logId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getWeeklyAverageCalories(): Int {
        val endDate = System.currentTimeMillis()
        val startDate = endDate - (7 * 24 * 60 * 60 * 1000)
        
        // This is a simplified implementation
        // In production, you'd want to calculate this from the daily totals
        return 2000 // TODO: Calculate from actual data
    }
    
    override suspend fun getWeeklyAverageMacros(): Triple<Double, Double, Double> {
        val endDate = System.currentTimeMillis()
        val startDate = endDate - (7 * 24 * 60 * 60 * 1000)
        
        // This is a simplified implementation
        // In production, you'd want to calculate this from the daily totals
        return Triple(150.0, 200.0, 70.0) // TODO: Calculate from actual data
    }
}
