package com.santelocale.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "VERT", "JAUNE", "ROUGE"
    val imageUrl: String,
    val tip: String
)
