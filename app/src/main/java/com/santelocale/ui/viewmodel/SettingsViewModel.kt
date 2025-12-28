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

    val userName: StateFlow<String> = userPreferencesRepository.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val glucoseUnit: StateFlow<String> = userPreferencesRepository.glucoseUnit
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferencesRepository.DEFAULT_UNIT)

    fun updateUserName(name: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserName(name)
        }
    }

    fun updateGlucoseUnit(unit: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateGlucoseUnit(unit)
        }
    }

    /**
     * Clears all data: health logs and user preferences.
     * Matches React's handleClear function.
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
