package com.santelocale.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.santelocale.data.database.HealthDatabase
import java.io.IOException

private const val TAG = "FoodDataLoader"
private const val CONTENT_FILE = "content.json"

/**
 * Loads food data from assets/content.json and inserts into the database.
 * Should be called from a coroutine (IO dispatcher).
 *
 * @param context Application context for accessing assets
 * @param database HealthDatabase instance for inserting food items
 */
suspend fun loadFoodData(context: Context, database: HealthDatabase) {
    try {
        // Read JSON from assets
        val jsonString = readJsonFromAssets(context)
        if (jsonString == null) {
            Log.e(TAG, "Failed to read $CONTENT_FILE from assets")
            return
        }

        // Parse JSON using Gson
        val gson = Gson()
        val wrapper = gson.fromJson(jsonString, FoodJsonWrapper::class.java)

        // Convert to FoodItem entities
        val foodItems = wrapper.foods.map { it.toFoodItem() }

        // Insert into database using foodItemDao
        database.foodItemDao().insertAll(foodItems)

        Log.d(TAG, "Successfully loaded ${foodItems.size} food items from $CONTENT_FILE")
    } catch (e: Exception) {
        Log.e(TAG, "Error loading food data: ${e.message}", e)
    }
}

/**
 * Reads the content.json file from the assets folder.
 * @return The JSON string, or null if reading failed.
 */
private fun readJsonFromAssets(context: Context): String? {
    return try {
        context.assets.open(CONTENT_FILE).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        Log.e(TAG, "Error reading $CONTENT_FILE: ${e.message}", e)
        null
    }
}
