package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Glucose Input screen.
 * Manages the custom keypad input state and saving glucose readings.
 */
class GlucoseViewModel(
    private val repository: HealthRepository
) : ViewModel() {

    private val _inputValue = MutableStateFlow("")
    val inputValue: StateFlow<String> = _inputValue.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Clear the error state.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Append a digit to the input value.
     * Limits to 5 characters to prevent overflow.
     */
    fun appendDigit(digit: String) {
        if (_inputValue.value.length < 5) {
            _inputValue.value += digit
        }
    }

    /**
     * Append decimal separator (comma for French formatting).
     * Only allows one comma, and adds "0," if input is empty.
     */
    fun appendDecimal() {
        val current = _inputValue.value
        if (!current.contains(",")) {
            _inputValue.value = if (current.isEmpty()) "0," else "$current,"
        }
    }

    /**
     * Delete the last character from input.
     */
    fun deleteLastDigit() {
        val current = _inputValue.value
        if (current.isNotEmpty()) {
            _inputValue.value = current.dropLast(1)
        }
    }

    /**
     * Clear the entire input.
     */
    fun clearInput() {
        _inputValue.value = ""
    }

    /**
     * Save the glucose reading to the database.
     * Converts comma to dot for storage, keeps comma for display.
     * @param unit The glucose unit (e.g., "mg/dL" or "mmol/L") - stored for reference
     * @param context The context of the reading (e.g., "Avant repas", "Après repas")
     * @return true if saved successfully, false otherwise
     */
    fun saveGlucose(unit: String, context: String? = null): Boolean {
        val displayValue = _inputValue.value
        if (displayValue.isEmpty()) return false

        // Convert comma to dot for numeric parsing
        val numericValue = displayValue.replace(",", ".").toFloatOrNull()
        if (numericValue == null) {
            _error.value = "error_invalid_value"
            return false
        }

        // Validation based on unit
        val (min, max) = if (unit == "mmol/L") 1.1f to 33.3f else 20f to 600f
        
        if (numericValue < min) {
            _error.value = "error_value_too_low"
            return false
        }
        if (numericValue > max) {
            _error.value = "error_value_too_high"
            return false
        }

        // Construct label with context if provided
        val finalLabel = if (context != null) "$unit • $context" else unit

        viewModelScope.launch {
            repository.insertLog(
                HealthLog(
                    type = "GLUCOSE",
                    value = numericValue,
                    displayValue = displayValue,
                    label = finalLabel, // Store the unit and context
                    date = System.currentTimeMillis()
                )
            )
            // Clear input after saving
            _inputValue.value = ""
        }
        return true
    }

    /**
     * Check if input is valid for saving.
     */
    fun canSave(): Boolean {
        val value = _inputValue.value
        return value.isNotEmpty() && value != "0," && value.replace(",", ".").toFloatOrNull() != null
    }
}

class GlucoseViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GlucoseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GlucoseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
