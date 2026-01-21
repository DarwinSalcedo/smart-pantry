package com.smart.pantry.presentation.pantry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.pantry.domain.model.PantryItem
import com.smart.pantry.domain.model.StorageLocation
import com.smart.pantry.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * UI State for Pantry Screen
 */
data class PantryUiState(
    val fridgeItems: List<PantryItem> = emptyList(),
    val pantryItems: List<PantryItem> = emptyList(),
    val freezerItems: List<PantryItem> = emptyList(),
    val expiringItems: List<PantryItem> = emptyList(),
    val expiredItems: List<PantryItem> = emptyList(),
    val selectedLocation: StorageLocation = StorageLocation.FRIDGE,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for Pantry Screen
 */
class PantryViewModel(
    private val getPantryItemsByLocation: GetPantryItemsByLocationUseCase,
    private val getExpiringItems: GetExpiringItemsUseCase,
    private val getExpiredItems: GetExpiredItemsUseCase,
    private val addPantryItem: AddPantryItemUseCase,
    private val updateItemQuantity: UpdateItemQuantityUseCase,
    private val deletePantryItem: DeletePantryItemUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PantryUiState())
    val uiState: StateFlow<PantryUiState> = _uiState.asStateFlow()
    
    init {
        loadPantryData()
    }
    
    private fun loadPantryData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                // Load items from all locations
                combine(
                    getPantryItemsByLocation(StorageLocation.FRIDGE),
                    getPantryItemsByLocation(StorageLocation.PANTRY),
                    getPantryItemsByLocation(StorageLocation.FREEZER),
                    getExpiringItems(withinDays = 3),
                    getExpiredItems()
                ) { fridge, pantry, freezer, expiring, expired ->
                    PantryUiState(
                        fridgeItems = fridge,
                        pantryItems = pantry,
                        freezerItems = freezer,
                        expiringItems = expiring,
                        expiredItems = expired,
                        selectedLocation = _uiState.value.selectedLocation,
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
    
    fun selectLocation(location: StorageLocation) {
        _uiState.update { it.copy(selectedLocation = location) }
    }
    
    fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            updateItemQuantity(itemId, newQuantity)
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }
    
    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            deletePantryItem(itemId)
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }
    
    fun addItem(item: PantryItem) {
        viewModelScope.launch {
            addPantryItem(item)
                .onSuccess {
                    // Item added successfully, data will refresh automatically via Flow
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun getCurrentItems(): List<PantryItem> {
        return when (_uiState.value.selectedLocation) {
            StorageLocation.FRIDGE -> _uiState.value.fridgeItems
            StorageLocation.PANTRY -> _uiState.value.pantryItems
            StorageLocation.FREEZER -> _uiState.value.freezerItems
        }
    }
}
