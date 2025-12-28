package com.santelocale.ui.screens

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.santelocale.data.entity.FoodItem
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.viewmodel.FoodViewModel

// Colors - Updated for semantic themes
private val Emerald100 = Color(0xFFD1FAE5)
private val Emerald600 = Color(0xFF059669)
private val Emerald700 = Color(0xFF047857)

private val Orange50 = Color(0xFFFFF7ED)
private val Orange100 = Color(0xFFFFEDD5)
private val Orange500 = Color(0xFFF97316)
private val Orange700 = Color(0xFFC2410C)

private val Red50 = Color(0xFFFEF2F2)
private val Red100 = Color(0xFFFEE2E2)
private val Red500 = Color(0xFFEF4444)
private val Red700 = Color(0xFFB91C1C)

private val Slate50 = Color(0xFFF8FAFC)
private val Slate400 = Color(0xFF94A3B8)
private val Slate800 = Color(0xFF1E293B)
private val Amber500 = Color(0xFFF59E0B)

/**
 * Food Guide screen with pill-style tab switcher and clean card list.
 */
@Composable
fun FoodGuideScreen(
    viewModel: FoodViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val foods by viewModel.foods.collectAsState()

    CurvedScreenWrapper(
        title = "Guide Alimentaire",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Pill Switcher - Using Surface for pure white
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tab 1: À VOLONTÉ (Green)
                    PillTab(
                        text = "À VOLONTÉ",
                        isSelected = selectedCategory == "VERT",
                        activeBackgroundColor = Emerald100,
                        activeTextColor = Emerald700,
                        onClick = { viewModel.selectCategory("VERT") },
                        modifier = Modifier.weight(1f)
                    )
                    // Tab 2: MODÉRÉMENT (Orange)
                    PillTab(
                        text = "MODÉRÉMENT",
                        isSelected = selectedCategory == "JAUNE",
                        activeBackgroundColor = Orange100,
                        activeTextColor = Orange700,
                        onClick = { viewModel.selectCategory("JAUNE") },
                        modifier = Modifier.weight(1f)
                    )
                    // Tab 3: À ÉVITER (Red)
                    PillTab(
                        text = "À ÉVITER",
                        isSelected = selectedCategory == "ROUGE",
                        activeBackgroundColor = Red100,
                        activeTextColor = Red700,
                        onClick = { viewModel.selectCategory("ROUGE") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Food List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(foods, key = { it.id }) { food ->
                    FoodCard(food = food)
                }
            }
        }
    }
}

/**
 * Pill-style tab button.
 * Selected: Custom bg/text, ExtraBold
 * Unselected: Transparent bg, Slate400 text, Bold
 */
@Composable
private fun PillTab(
    text: String,
    isSelected: Boolean,
    activeBackgroundColor: Color,
    activeTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) activeBackgroundColor else Color.Transparent
    val textColor = if (isSelected) activeTextColor else Slate400
    val fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = fontWeight,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

/**
 * Food card with clean shadow (no border).
 * White Card, RoundedCornerShape(24.dp), elevation 2dp.
 * Image: 80dp square, RoundedCornerShape(16.dp).
 */
@Composable
private fun FoodCard(food: FoodItem) {
    var expanded by remember { mutableStateOf(false) }

    // Category colors
    val (categoryColor, tipBackgroundColor) = when (food.category) {
        "VERT" -> Emerald600 to Slate50
        "JAUNE" -> Orange500 to Orange50
        "ROUGE" -> Red500 to Red50
        else -> Color.Gray to Slate50
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image: 80dp square, RoundedCornerShape(16.dp)
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = categoryColor.copy(alpha = 0.1f)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(food.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = food.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                        loading = {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = categoryColor
                                )
                            }
                        },
                        error = {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Image,
                                    contentDescription = null,
                                    tint = categoryColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Text Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                    color = tipBackgroundColor,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
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