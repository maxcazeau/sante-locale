package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the Activity Input screen.
 * Handles saving activity logs to the database.
 */
class ActivityViewModel(
    private val repository: HealthRepository
) : ViewModel() {

    /**
     * Save an activity log to the database.
     * @param label The activity label (e.g., "Marche (30 min)")
     * @param minutes The duration in minutes
     */
    fun saveActivity(label: String, minutes: Int) {
        viewModelScope.launch {
            repository.insertLog(
                HealthLog(
                    type = "ACTIVITY",
                    value = minutes.toFloat(),
                    displayValue = null,
                    label = label,
                    date = System.currentTimeMillis()
                )
            )
        }
    }
}

class ActivityViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
