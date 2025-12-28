package com.santelocale.data

import com.santelocale.data.entity.FoodItem

/**
 * Wrapper class for parsing the JSON structure: {"foods": [...]}
 */
data class FoodJsonWrapper(
    val foods: List<FoodItemJson>
)

/**
 * JSON representation of a food item.
 * Maps to the FoodItem entity.
 */
data class FoodItemJson(
    val id: String,
    val name: String,
    val category: String,
    val imageUrl: String,
    val tip: String
) {
    fun toFoodItem(): FoodItem = FoodItem(
        id = id,
        name = name,
        category = category,
        imageUrl = imageUrl,
        tip = tip
    )
}
