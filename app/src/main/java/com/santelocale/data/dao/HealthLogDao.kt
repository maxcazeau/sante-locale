package com.santelocale.data.dao

import androidx.room.*
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthLogDao {
    @Query("SELECT * FROM health_logs ORDER BY date DESC")
    fun getAllLogs(): Flow<List<HealthLog>>

    @Query("SELECT * FROM health_logs WHERE type = 'GLUCOSE' ORDER BY date DESC LIMIT 1")
    fun getLastGlucose(): Flow<HealthLog?>

    @Insert
    suspend fun insert(log: HealthLog)

    @Delete
    suspend fun delete(log: HealthLog)

    @Query("DELETE FROM health_logs")
    suspend fun deleteAll()
}
