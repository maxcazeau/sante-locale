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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.R
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.viewmodel.ActivityViewModel

// Colors - New teal/green harmony palette
private val Slate300 = Color(0xFFCBD5E1)
private val Slate400 = Color(0xFF94A3B8)
private val Slate700 = Color(0xFF334155)
private val Slate800 = Color(0xFF1E293B)
private val PrimaryGreen = Color(0xFF0E7C66)
private val AccentTeal = Color(0xFF1BA6A6)
private val TealLight = Color(0xFFE0F2F1)

private data class ActivityType(
    val label: String,
    val icon: ImageVector
)

private val activityTypes = listOf(
    ActivityType("Marche", Icons.AutoMirrored.Rounded.DirectionsWalk),
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
        title = stringResource(R.string.title_activity),
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // 1. Large Main Icon (Animated)
                    AnimatedIcon(
                        icon = selectedActivity.icon,
                        contentDescription = selectedActivity.label
                    )

                    // 2. Question Text
                    Text(
                        text = stringResource(R.string.activity_question),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    // 3. Time Selector
                    TimeSelector(
                        minutes = minutes,
                        onMinutesChange = { minutes = it }
                    )

                    // 4. Activity Grid
                    ActivityGrid(
                        selectedActivity = selectedActivity,
                        onActivitySelected = { selectedActivity = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.saveActivity(selectedActivity.label, minutes)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_save_normal),
                    fontSize = 18.sp,
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
        color = TealLight,
        modifier = Modifier.size(56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AnimatedContent(targetState = icon, label = "icon_anim") { targetIcon ->
                Icon(
                    imageVector = targetIcon,
                    contentDescription = contentDescription,
                    tint = AccentTeal,
                    modifier = Modifier.size(28.dp)
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
        // Minus Button
        IconButton(
            onClick = { if (minutes > 5) onMinutesChange(minutes - 5) },
            modifier = Modifier
                .size(48.dp)
                .background(Slate300.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = stringResource(R.string.cd_decrease),
                tint = Slate700,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Value Display
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = minutes.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = Slate800,
                lineHeight = 48.sp
            )
            Text(
                text = stringResource(R.string.label_minutes),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Slate400
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Plus Button
        IconButton(
            onClick = { if (minutes < 300) onMinutesChange(minutes + 5) },
            modifier = Modifier
                .size(48.dp)
                .background(Slate300.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.cd_increase),
                tint = Slate700,
                modifier = Modifier.size(24.dp)
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(130.dp)
    ) {
        items(activityTypes) { activity ->
            val isSelected = activity == selectedActivity

            Surface(
                modifier = Modifier
                    .height(58.dp)
                    .clickable { onActivitySelected(activity) }
                    .then(if (!isSelected) Modifier.alpha(0.8f) else Modifier),
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) TealLight else Color(0xFFF5F5F5),
                border = if (isSelected) {
                    BorderStroke(1.5.dp, AccentTeal)
                } else {
                    BorderStroke(1.dp, Color(0xFFE0E0E0))
                },
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = activity.icon,
                        contentDescription = null,
                        tint = if (isSelected) PrimaryGreen else Slate400,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = activity.label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) PrimaryGreen else Slate700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}