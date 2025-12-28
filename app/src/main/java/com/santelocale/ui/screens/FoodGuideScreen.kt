package com.santelocale.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.santelocale.data.entity.FoodItem
import com.santelocale.ui.components.Header
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.FoodViewModel

// Category configuration matching React mockup
private data class CategoryConfig(
    val label: String,
    val color: Color,
    val textColor: Color,
    val borderColor: Color,
    val description: String
)

private val categories = mapOf(
    "VERT" to CategoryConfig(
        label = "À VOLONTÉ",
        color = Emerald600,
        textColor = Emerald700,
        borderColor = Color(0xFFA7F3D0), // emerald-200
        description = "Mangez tous les jours."
    ),
    "JAUNE" to CategoryConfig(
        label = "MODÉRÉMENT",
        color = Yellow500,
        textColor = Color(0xFFA16207), // yellow-700
        borderColor = Color(0xFFFEF08A), // yellow-200
        description = "Petites quantités seulement."
    ),
    "ROUGE" to CategoryConfig(
        label = "À ÉVITER",
        color = Red600,
        textColor = Color(0xFFB91C1C), // red-700
        borderColor = Color(0xFFFECACA), // red-200
        description = "Dangereux pour le sucre."
    )
)

// Additional colors
private val Blue500 = Color(0xFF3B82F6)
private val Blue100 = Color(0xFFDBEAFE)

/**
 * Food Guide screen with traffic light category tabs.
 * Matches the React FoodGuide component design.
 */
@Composable
fun FoodGuideScreen(
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val foods by viewModel.foods.collectAsState()
    val expandedFoodId by viewModel.expandedFoodId.collectAsState()

    val currentConfig = categories[selectedCategory] ?: categories["VERT"]!!

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header
        Header(
            title = "Guide Alimentaire",
            onBack = onBack
        )

        // Category Tab Row
        CategoryTabs(
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.selectCategory(it) }
        )

        // Description Banner
        DescriptionBanner(
            category = selectedCategory,
            config = currentConfig
        )

        // Food List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(foods, key = { it.id }) { food ->
                FoodCard(
                    food = food,
                    isExpanded = expandedFoodId == food.id,
                    onClick = { viewModel.toggleFoodExpanded(food.id) }
                )
            }
        }
    }
}

/**
 * Category tab row with three tabs: VERT, JAUNE, ROUGE
 */
@Composable
private fun CategoryTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        listOf("VERT", "JAUNE", "ROUGE").forEach { category ->
            val config = categories[category]!!
            val isSelected = selectedCategory == category

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) config.color else White
                    )
                    .then(
                        if (!isSelected) {
                            Modifier.border(
                                width = 0.dp,
                                color = Color.Transparent
                            )
                        } else Modifier
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = if (isSelected) White else Slate500
                )
            }

            // Bottom border for unselected tabs
            if (!isSelected) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(Slate100)
                )
            }
        }
    }
}

/**
 * Description banner below tabs showing category guidance.
 */
@Composable
private fun DescriptionBanner(
    category: String,
    config: CategoryConfig,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = config.borderColor
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on category
            when (category) {
                "VERT" -> Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = config.textColor,
                    modifier = Modifier.size(16.dp)
                )
                "ROUGE" -> Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = config.textColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            if (category == "VERT" || category == "ROUGE") {
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = config.description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = config.textColor
            )
        }
    }
}

/**
 * Individual food card with image, name, and expandable tip.
 */
@Composable
private fun FoodCard(
    food: FoodItem,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = if (isExpanded) Blue500 else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (isExpanded) {
                    Modifier.border(
                        width = 4.dp,
                        color = Blue100,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp,
        color = White
    ) {
        Column {
            // Image with gradient overlay and title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp)
            ) {
                // Food image
                AsyncImage(
                    model = food.imageUrl,
                    contentDescription = food.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay at bottom
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.8f)
                                )
                            )
                        )
                )

                // Food name
                Text(
                    text = food.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            // Expandable tip section
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Slate50
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = Slate100
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "\uD83D\uDCA1", // 💡 lightbulb emoji
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = food.tip,
                            fontSize = 16.sp,
                            color = Slate700,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}
