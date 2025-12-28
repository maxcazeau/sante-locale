package com.santelocale.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: HealthRepository
) : ViewModel() {

    val allLogs: StateFlow<List<HealthLog>> = repository.allLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val lastGlucose: StateFlow<HealthLog?> = repository.lastGlucose
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun insertLog(log: HealthLog) {
        viewModelScope.launch {
            repository.insertLog(log)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllLogs()
        }
    }
}

class DashboardViewModelFactory(
    private val repository: HealthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
