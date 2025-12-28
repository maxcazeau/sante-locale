package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for the History screen.
 * Exposes all health logs sorted by date descending.
 */
class HistoryViewModel(
    repository: HealthRepository
) : ViewModel() {

    /**
     * All health logs (glucose and activity) sorted by date descending.
     */
    val allLogs: StateFlow<List<HealthLog>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

class HistoryViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
