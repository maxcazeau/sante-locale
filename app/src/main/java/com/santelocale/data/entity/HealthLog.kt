package com.santelocale.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "GLUCOSE" or "ACTIVITY"
    val value: Float, // Numeric value for calculations
    val displayValue: String?, // What the user saw (e.g. "5,4")
    val label: String? = null, // For activity type (e.g. "Marche (30 min)")
    val date: Long // Epoch timestamp
)
