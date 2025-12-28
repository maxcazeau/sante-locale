package com.santelocale.data.repository

import com.santelocale.data.dao.FoodItemDao
import com.santelocale.data.dao.HealthLogDao
import com.santelocale.data.entity.FoodItem
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.flow.Flow

class HealthRepository(
    private val healthLogDao: HealthLogDao,
    private val foodItemDao: FoodItemDao
) {
    // Health Logs
    val allLogs: Flow<List<HealthLog>> = healthLogDao.getAllLogs()
    val lastGlucose: Flow<HealthLog?> = healthLogDao.getLastGlucose()

    suspend fun insertLog(log: HealthLog) {
        healthLogDao.insert(log)
    }

    suspend fun deleteLog(log: HealthLog) {
        healthLogDao.delete(log)
    }

    suspend fun clearAllLogs() {
        healthLogDao.deleteAll()
    }

    // Food Items
    fun getFoodsByCategory(category: String): Flow<List<FoodItem>> {
        return foodItemDao.getFoodsByCategory(category)
    }

    fun getAllFoods(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoods()
    }
}
