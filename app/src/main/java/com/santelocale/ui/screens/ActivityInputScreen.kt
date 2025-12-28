package com.santelocale.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.viewmodel.ActivityViewModel

// Colors
private val Slate300 = Color(0xFFCBD5E1)
private val Slate400 = Color(0xFF94A3B8)
private val Slate700 = Color(0xFF334155)
private val Slate800 = Color(0xFF1E293B)
private val Emerald50 = Color(0xFFECFDF5)
private val Emerald500 = Color(0xFF10B981)
private val Emerald600 = Color(0xFF059669)

private data class ActivityType(
    val label: String,
    val icon: ImageVector
)

private val activityTypes = listOf(
    ActivityType("Marche", Icons.Rounded.DirectionsWalk),
    ActivityType("Ménage", Icons.Rounded.CleaningServices),
    ActivityType("Jardinage", Icons.Rounded.Park),
    ActivityType("Autres", Icons.Rounded.MoreHoriz)
)

@Composable
fun ActivityInputScreen(
    viewModel: ActivityViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedActivity by remember { mutableStateOf(activityTypes[0]) }
    var minutes by remember { mutableIntStateOf(30) }

    CurvedScreenWrapper(
        title = "Activité",
        onBack = onBack,
        headerHeight = 220.dp // Increased header height
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), // Enable scrolling
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp), // Reduced from 24dp to save horizontal space
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Large Main Icon (Animated)
                    AnimatedIcon(
                        icon = selectedActivity.icon,
                        contentDescription = selectedActivity.label
                    )

                    Spacer(modifier = Modifier.height(24.dp)) // Increased spacing

                    // 2. Question Text (Increased size and weight)
                    Text(
                        text = "Combien de minutes d'activité avez-vous fait ?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp)) // Increased spacing

                    // 3. Time Selector (Massive counter)
                    TimeSelector(
                        minutes = minutes,
                        onMinutesChange = { minutes = it }
                    )

                    Spacer(modifier = Modifier.height(40.dp)) // Increased spacing

                    // 4. Activity Grid (Larger icons and thicker borders)
                    ActivityGrid(
                        selectedActivity = selectedActivity,
                        onActivitySelected = { selectedActivity = it }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp)) // Padding inside card bottom
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button (Taller, simpler label, wider margins)
            Button(
                onClick = {
                    viewModel.saveActivity(selectedActivity.label, minutes)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp) // Height increased to 80px (approx 80dp)
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp), // Wider margins
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Enregistrer", // Removed "l'activité"
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedIcon(
    icon: ImageVector,
    contentDescription: String
) {
    Surface(
        shape = CircleShape,
        color = Emerald50,
        modifier = Modifier.size(96.dp) // Slightly larger if needed, kept balanced
    ) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedContent(targetState = icon, label = "icon_anim") { targetIcon ->
                Icon(
                    imageVector = targetIcon,
                    contentDescription = contentDescription,
                    tint = Emerald600,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun TimeSelector(
    minutes: Int,
    onMinutesChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Minus Button (Larger: 64dp)
        IconButton(
            onClick = { if (minutes > 5) onMinutesChange(minutes - 5) },
            modifier = Modifier
                .size(64.dp)
                .background(Slate300.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Diminuer",
                tint = Slate700,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Value Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = minutes.toString(),
                fontSize = 80.sp, // Massive text (approx 96px might be too big for layout, trying 80sp)
                fontWeight = FontWeight.Black,
                color = Slate800,
                lineHeight = 80.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp)) // Increased separation
            
            Text(
                text = "Minutes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Slate400
            )
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Plus Button (Larger: 64dp)
        IconButton(
            onClick = { if (minutes < 300) onMinutesChange(minutes + 5) },
            modifier = Modifier
                .size(64.dp)
                .background(Slate300.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Augmenter",
                tint = Slate700,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun ActivityGrid(
    selectedActivity: ActivityType,
    onActivitySelected: (ActivityType) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Increased spacing
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.height(180.dp) // Increased height for larger items
    ) {
        items(activityTypes) { activity ->
            val isSelected = activity == selectedActivity
            
            Surface(
                modifier = Modifier
                    .height(80.dp) // Increased item height
                    .clickable { onActivitySelected(activity) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Emerald50 else Color.White,
                // Thicker border for selected state
                border = if (isSelected) BorderStroke(3.dp, Emerald500) else BorderStroke(1.dp, Slate300.copy(alpha = 0.5f)),
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp), // Reduced padding from 20dp to 12dp
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = if (isSelected) Emerald600 else Slate400,
                        modifier = Modifier.size(32.dp) // Larger icons (text-3xl approx)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp)) // Reduced spacing from 16dp to 12dp
                    
                    Text(
                        text = activity.label,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Emerald600 else Slate700,
                        maxLines = 1, // Prevent multiline breaks
                        overflow = TextOverflow.Ellipsis // Handle overflow gracefully
                    )
                }
            }
        }
    }
}