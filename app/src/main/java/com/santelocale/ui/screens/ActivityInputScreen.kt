package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.components.Header
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.ActivityViewModel

/**
 * Activity data class for the preset buttons.
 */
private data class Activity(
    val label: String,
    val emoji: String,
    val minutes: Int
)

/**
 * Preset activities matching the React mockup.
 */
private val activities = listOf(
    Activity(label = "Marche (30 min)", emoji = "\uD83D\uDEB6\uD83C\uDFFE\u200D\u2642\uFE0F", minutes = 30), // 🚶🏾‍♂️
    Activity(label = "Ménage", emoji = "\uD83E\uDDF9", minutes = 20), // 🧹
    Activity(label = "Jardinage", emoji = "\uD83C\uDF3F", minutes = 45), // 🌿
    Activity(label = "Danse / Sport", emoji = "\uD83D\uDC83\uD83C\uDFFE", minutes = 30) // 💃🏾
)

/**
 * Activity Input screen with preset activity buttons.
 * Matches the React ActivityInput component design.
 * When an activity is clicked, it saves immediately and navigates back.
 */
@Composable
fun ActivityInputScreen(
    viewModel: ActivityViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header
        Header(
            title = "Activité",
            onBack = onBack
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Question text
            Text(
                text = "Qu'avez-vous fait aujourd'hui ?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Slate700,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Activity buttons grid (2x2)
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Row 1: Marche, Ménage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActivityButton(
                        activity = activities[0],
                        onClick = {
                            viewModel.saveActivity(activities[0].label, activities[0].minutes)
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ActivityButton(
                        activity = activities[1],
                        onClick = {
                            viewModel.saveActivity(activities[1].label, activities[1].minutes)
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: Jardinage, Sport
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActivityButton(
                        activity = activities[2],
                        onClick = {
                            viewModel.saveActivity(activities[2].label, activities[2].minutes)
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    ActivityButton(
                        activity = activities[3],
                        onClick = {
                            viewModel.saveActivity(activities[3].label, activities[3].minutes)
                            onBack()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Large square activity button with emoji and label.
 * Matches React: bg-white p-6 rounded-2xl shadow-md border-2
 * hover:border-emerald-500 active:scale-95
 */
@Composable
private fun ActivityButton(
    activity: Activity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation on press
    val scale = if (isPressed) 0.95f else 1f

    Surface(
        modifier = modifier
            .aspectRatio(1f) // Square button
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = if (isPressed) Emerald500 else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Large emoji
            Text(
                text = activity.emoji,
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Activity label
            Text(
                text = activity.label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate700,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            // Duration badge
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${activity.minutes} min",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Slate500
            )
        }
    }
}
