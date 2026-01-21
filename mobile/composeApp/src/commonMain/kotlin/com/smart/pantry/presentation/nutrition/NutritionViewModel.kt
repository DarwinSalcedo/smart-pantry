package com.smart.pantry.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.pantry.domain.model.DailyNutrition
import com.smart.pantry.domain.model.NutritionLog
import com.smart.pantry.domain.usecase.AddNutritionLogUseCase
import com.smart.pantry.domain.usecase.GetDailyNutritionTotalsUseCase
import com.smart.pantry.domain.usecase.GetNutritionLogsByDateUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * UI State for Nutrition Screen
 */
data class NutritionUiState(
    val todayLogs: List<NutritionLog> = emptyList(),
    val weeklyTotals: List<DailyNutrition> = emptyList(),
    val selectedDate: Long = System.currentTimeMillis(),
    val totalCalories: Int = 0,
    val totalProtein: Double = 0.0,
    val totalCarbs: Double = 0.0,
    val totalFat: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for Nutrition Tracking Screen
 */
class NutritionViewModel(
    private val getNutritionLogsByDate: GetNutritionLogsByDateUseCase,
    private val getDailyNutritionTotals: GetDailyNutritionTotalsUseCase,
    private val addNutritionLog: AddNutritionLogUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()
    
    init {
        loadNutritionData()
    }
    
    private fun loadNutritionData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val today = _uiState.value.selectedDate
                val weekAgo = today - (7 * 24 * 60 * 60 * 1000)
                
                combine(
                    getNutritionLogsByDate(today),
                    getDailyNutritionTotals(weekAgo, today)
                ) { todayLogs, weeklyTotals ->
                    val totals = todayLogs.fold(
                        Triple(0, 0.0, 0.0) to 0.0
                    ) { acc, log ->
                        Triple(
                            acc.first.first + log.calories,
                            acc.first.second + log.proteinG,
                            acc.first.third + log.carbsG
                        ) to (acc.second + log.fatG)
                    }
                    
                    NutritionUiState(
                        todayLogs = todayLogs,
                        weeklyTotals = weeklyTotals,
                        selectedDate = today,
                        totalCalories = totals.first.first,
                        totalProtein = totals.first.second,
                        totalCarbs = totals.first.third,
                        totalFat = totals.second,
                        isLoading = false
                    )
                }.collect { state ->
                    _uiState.value = state
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
    
    fun addLog(log: NutritionLog) {
        viewModelScope.launch {
            addNutritionLog(log)
                .onSuccess {
                    loadNutritionData()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }
    
    fun selectDate(date: Long) {
        _uiState.update { it.copy(selectedDate = date) }
        loadNutritionData()
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
