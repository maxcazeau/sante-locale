package com.santelocale.data.dao

import androidx.room.*
import com.santelocale.data.entity.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_items WHERE category = :category ORDER BY name")
    fun getFoodsByCategory(category: String): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_items")
    fun getAllFoods(): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(foods: List<FoodItem>)

    @Query("SELECT COUNT(*) FROM food_items")
    suspend fun getCount(): Int
}
