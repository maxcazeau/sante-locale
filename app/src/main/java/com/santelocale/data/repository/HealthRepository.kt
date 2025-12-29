package com.santelocale.data.repository

import com.santelocale.data.dao.FoodItemDao
import com.santelocale.data.dao.HealthLogDao
import com.santelocale.data.entity.FoodItem
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.flow.Flow

/**
 * Single entry point for managing health data and food information.
 * Coordinates between local DAOs to provide data streams to ViewModels.
 */
class HealthRepository(
    private val healthLogDao: HealthLogDao,
    private val foodItemDao: FoodItemDao
) {
    /**
     * Stream of all health logs (glucose and activity) ordered by date descending.
     */
    val allLogs: Flow<List<HealthLog>> = healthLogDao.getAllLogs()

    /**
     * Stream of the most recent glucose entry.
     */
    val lastGlucose: Flow<HealthLog?> = healthLogDao.getLastGlucose()

    /**
     * Persist a new health measurement.
     */
    suspend fun insertLog(log: HealthLog) {
        healthLogDao.insert(log)
    }

    /**
     * Remove a specific log entry.
     */
    suspend fun deleteLog(log: HealthLog) {
        healthLogDao.delete(log)
    }

    /**
     * Wipe all user health data.
     */
    suspend fun clearAllLogs() {
        healthLogDao.deleteAll()
    }

    /**
     * Get food items filtered by health impact category (VERT, JAUNE, ROUGE).
     */
    fun getFoodsByCategory(category: String): Flow<List<FoodItem>> {
        return foodItemDao.getFoodsByCategory(category)
    }

    /**
     * Get the entire food database.
     */
    fun getAllFoods(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoods()
    }
}
