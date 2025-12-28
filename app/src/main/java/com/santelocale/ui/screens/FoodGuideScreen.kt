package com.santelocale.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.santelocale.data.entity.FoodItem
import com.santelocale.ui.components.Header
import com.santelocale.ui.viewmodel.FoodViewModel

// Colors
private val Emerald600 = Color(0xFF059669)
private val Yellow600 = Color(0xFFCA8A04)
private val Red600 = Color(0xFFDC2626)
private val Slate50 = Color(0xFFF8FAFC)
private val Amber500 = Color(0xFFF59E0B)

/**
 * Food Guide screen with ScrollableTabRow and row-based card layout.
 */
@Composable
fun FoodGuideScreen(
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val foods by viewModel.foods.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header
        Header(title = "Guide Alimentaire", onBack = onBack)

        // ScrollableTabRow prevents "MODÉRÉMENT" from cutting off
        ScrollableTabRow(
            selectedTabIndex = when (selectedCategory) {
                "VERT" -> 0
                "JAUNE" -> 1
                else -> 2
            },
            containerColor = Color.White,
            contentColor = Color.Black,
            edgePadding = 0.dp,
            divider = {},
            indicator = { tabPositions ->
                val selectedIndex = when (selectedCategory) {
                    "VERT" -> 0
                    "JAUNE" -> 1
                    else -> 2
                }
                val indicatorColor = when (selectedCategory) {
                    "VERT" -> Emerald600
                    "JAUNE" -> Yellow600
                    else -> Red600
                }
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(indicatorColor)
                )
            }
        ) {
            FoodTab("À VOLONTÉ", selectedCategory == "VERT", Emerald600) {
                viewModel.selectCategory("VERT")
            }
            FoodTab("MODÉRÉMENT", selectedCategory == "JAUNE", Yellow600) {
                viewModel.selectCategory("JAUNE")
            }
            FoodTab("À ÉVITER", selectedCategory == "ROUGE", Red600) {
                viewModel.selectCategory("ROUGE")
            }
        }

        // Food List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(foods, key = { it.id }) { food ->
                FoodCardRow(food = food)
            }
        }
    }
}

/**
 * Tab component for category selection.
 */
@Composable
private fun FoodTab(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Tab(
        selected = isSelected,
        onClick = onClick,
        text = {
            Text(
                text = text,
                color = if (isSelected) color else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

/**
 * Row-based food card with image on left, text on right.
 * Expands to show health tip when clicked.
 */
@Composable
private fun FoodCardRow(food: FoodItem) {
    var expanded by remember { mutableStateOf(false) }

    // Determine border color based on category
    val categoryColor = when (food.category) {
        "VERT" -> Emerald600
        "JAUNE" -> Yellow600
        "ROUGE" -> Red600
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = if (expanded) BorderStroke(2.dp, categoryColor) else null
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Box (Left Side)
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = categoryColor.copy(alpha = 0.1f)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(food.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = food.name,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = categoryColor
                                )
                            }
                        },
                        error = {
                            // Fallback Icon when offline/broken URL
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Image,
                                    contentDescription = null,
                                    tint = categoryColor.copy(alpha = 0.5f)
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text Content (Right Side)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    // Mini-badge for category
                    Text(
                        text = when (food.category) {
                            "VERT" -> "Bon pour la santé"
                            "JAUNE" -> "Petite portion"
                            "ROUGE" -> "Attention danger"
                            else -> ""
                        },
                        fontSize = 12.sp,
                        color = categoryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Expanded Tip Section
            if (expanded) {
                Surface(
                    color = Slate50,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = "Tip",
                            tint = Amber500,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = food.tip,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
