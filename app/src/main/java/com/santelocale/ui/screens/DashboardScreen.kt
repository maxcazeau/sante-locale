package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.santelocale.R
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.entity.getGlucoseStatusDisplay
import com.santelocale.ui.viewmodel.DashboardViewModel

// Colors - New teal/green harmony palette
private val PrimaryGreen = Color(0xFF0E7C66)    // Header, primary actions
private val AccentTeal = Color(0xFF1BA6A6)      // CTA gradient start
private val TealLight = Color(0xFFB2DFDB)       // Teal subtitle tint
private val Orange500 = Color(0xFFF97316)
private val Orange50 = Color(0xFFFFF7ED)
private val Slate50 = Color(0xFFF4F6F7)         // Updated background
private val Slate100 = Color(0xFFF1F5F9)
private val Slate400 = Color(0xFF94A3B8)
private val Slate700 = Color(0xFF334155)
private val Slate800 = Color(0xFF1E293B)
private val TealStatus = Color(0xFFE0F2F1)    // Light teal for status pill bg
private val TealStatusText = Color(0xFF0E7C66) // Primary green for status text

/**
 * Enhanced Dashboard screen with curved header and overlapping status card.
 */
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    userName: String,
    unit: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastGlucose by viewModel.lastGlucose.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Curved Header
            CurvedHeader(
                userName = userName,
                onSettingsClick = { onNavigate("settings") }
            )

            // 2. Overlapping Status Card (offset up into header)
            StatusCard(
                lastLog = lastGlucose,
                unit = unit,
                onHistoryClick = { onNavigate("history") },
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-48).dp)
            )

            // 3. Action Grid
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-24).dp)
            ) {
                Text(
                    text = stringResource(R.string.label_quick_actions),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    modifier = Modifier.padding(start = 4.dp, bottom = 16.dp)
                )

                // Primary Action: Glucose (Full Width)
                GlucoseButton(
                    onClick = { onNavigate("glucose") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Secondary Actions: Food & Activity (Split Row)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ActionCard(
                        icon = Icons.Rounded.Restaurant,
                        label = stringResource(R.string.action_food),
                        iconTint = AccentTeal,
                        hoverColor = TealStatus,
                        onClick = { onNavigate("food") },
                        modifier = Modifier.weight(1f)
                    )

                    ActionCard(
                        icon = Icons.Rounded.DirectionsRun,
                        label = stringResource(R.string.action_activity),
                        iconTint = Orange500,
                        hoverColor = Orange50,
                        onClick = { onNavigate("activity") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Curved header with gradient background.
 * Height: 260dp, Rounded bottom corners: 32dp
 */
@Composable
private fun CurvedHeader(
    userName: String,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(AccentTeal, PrimaryGreen)
                )
            )
            .padding(24.dp)
    ) {
        Column {
            // Top Bar: Offline badge + Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Offline Badge (glass pill effect)
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(50)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WifiOff,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.offline_badge),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Settings Button
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(R.string.cd_settings),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Welcome Text
            Text(
                text = if (userName.isNotEmpty()) stringResource(R.string.greeting_user, userName) else stringResource(R.string.greeting_default),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            )
        }
    }
}

/**
 * Status card showing last glucose reading.
 * Overlaps header with elevation 8dp.
 */
@Composable
private fun StatusCard(
    lastLog: HealthLog?,
    unit: String,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.label_last_measure),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (lastLog != null) {
                    // Value display
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = lastLog.displayValue ?: lastLog.value.toInt().toString(),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = Slate800
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = unit,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status pill
                    val statusText = lastLog.getGlucoseStatusDisplay(unit)
                    Surface(
                        color = TealStatus,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = statusText,
                            color = TealStatusText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Text(
                        text = "-- ",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        color = Slate400
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.label_no_data),
                        fontSize = 14.sp,
                        color = Slate400
                    )
                }
            }

            // History Button
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onHistoryClick),
                shape = CircleShape,
                color = Slate100
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.History,
                        contentDescription = stringResource(R.string.cd_history),
                        tint = AccentTeal,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

/**
 * Primary glucose action button with blue gradient.
 * Full width, horizontal layout.
 */
@Composable
private fun GlucoseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(AccentTeal, PrimaryGreen)
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon in semi-transparent circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WaterDrop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = stringResource(R.string.action_glucose_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = stringResource(R.string.action_glucose_subtitle),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TealLight
                    )
                }
            }
        }
    }
}

/**
 * Secondary action card (Food/Activity).
 * Vertical layout with icon on top.
 */
@Composable
private fun ActionCard(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    hoverColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Slate700
            )
        }
    }
}
