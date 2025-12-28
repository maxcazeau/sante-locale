package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.entity.FoodItem
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the Food Guide screen.
 * Manages category selection and exposes filtered food items.
 */
class FoodViewModel(
    private val repository: HealthRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("VERT")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _expandedFoodId = MutableStateFlow<String?>(null)
    val expandedFoodId: StateFlow<String?> = _expandedFoodId.asStateFlow()

    /**
     * Foods filtered by the currently selected category.
     * Automatically updates when category changes.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val foods: StateFlow<List<FoodItem>> = _selectedCategory
        .flatMapLatest { category ->
            repository.getFoodsByCategory(category)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Select a category tab.
     * Also collapses any expanded food item.
     */
    fun selectCategory(category: String) {
        _selectedCategory.value = category
        _expandedFoodId.value = null
    }

    /**
     * Toggle the expanded state of a food item.
     * If the item is already expanded, collapse it.
     * If another item is expanded, switch to the new one.
     */
    fun toggleFoodExpanded(foodId: String) {
        _expandedFoodId.value = if (_expandedFoodId.value == foodId) null else foodId
    }
}

class FoodViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FoodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
