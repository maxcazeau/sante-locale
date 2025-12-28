package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.UserPreferencesRepository
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings screen.
 * Manages user preferences and data clearing functionality.
 */
class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val healthRepository: HealthRepository
) : ViewModel() {

    /**
     * User's name as StateFlow.
     */
    val userName: StateFlow<String> = userPreferencesRepository.userNameFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    /**
     * Glucose unit preference as StateFlow.
     */
    val glucoseUnit: StateFlow<String> = userPreferencesRepository.glucoseUnitFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "mg/dL")

    /**
     * Update the user's name.
     */
    fun updateName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveName(name)
        }
    }

    /**
     * Toggle/set the glucose unit.
     * @param unit Either "mg/dL" or "mmol/L"
     */
    fun toggleUnit(unit: String) {
        viewModelScope.launch {
            userPreferencesRepository.saveUnit(unit)
        }
    }

    /**
     * Clears all health data by calling healthRepository.deleteAll().
     * Also clears user preferences.
     */
    fun clearAllData() {
        viewModelScope.launch {
            healthRepository.clearAllLogs()
            userPreferencesRepository.clearPreferences()
        }
    }
}

class SettingsViewModelFactory(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val healthRepository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(userPreferencesRepository, healthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
