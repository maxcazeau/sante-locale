package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.data.entity.HealthLog
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.HistoryViewModel
import com.santelocale.utils.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * History screen showing all health logs.
 */
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    unit: String,
    userName: String = "",
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.allLogs.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    CurvedScreenWrapper(
        title = "Historique",
        onBack = onBack
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (logs.isEmpty()) {
                // Empty State - Large gray icon centered
                EmptyState()
            } else {
                // History List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(logs, key = { it.id }) { log ->
                        HistoryItemCard(
                            log = log,
                            unit = unit
                        )
                    }
                }
            }

            // FAB for PDF export
            if (logs.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            PdfGenerator.generateAndShare(
                                context = context,
                                userName = userName,
                                logs = logs,
                                unit = unit
                            )
                        }
                    },
                    containerColor = AccentTeal,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = "Exporter PDF"
                    )
                }
            }
        }
    }
}

/**
 * Empty state - Large gray History icon centered.
 */
@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.History,
                contentDescription = null,
                tint = Slate400.copy(alpha = 0.3f),
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aucun historique",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Slate400,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * History Item Card.
 * White Card, RoundedCornerShape(20.dp), elevation 2dp.
 * Layout: Row with circular icon (left), date/time (middle), value (right).
 */
@Composable
private fun HistoryItemCard(
    log: HealthLog,
    unit: String,
    modifier: Modifier = Modifier
) {
    val isGlucose = log.type == "GLUCOSE"

    // Colors based on type
    val iconBackground = if (isGlucose) TealStatus else Orange100
    val iconTint = if (isGlucose) AccentTeal else Orange500

    // Value display
    val displayValue = if (isGlucose) {
        log.displayValue ?: log.value.toInt().toString()
    } else {
        log.value.toInt().toString()
    }
    val valueUnit = if (isGlucose) unit else "min"

    // Format date and time separately
    val (formattedDate, formattedTime) = formatDateTime(log.date)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Circular Icon Background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
            Icon(
                imageVector = if (isGlucose) Icons.Rounded.WaterDrop else Icons.AutoMirrored.Rounded.DirectionsRun,
                contentDescription = null,
                tint = iconTint, // Fixed wrong tint reference from previous bad paste
                modifier = Modifier.size(24.dp)
            )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Middle: Date (Bold) and Time (Gray)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate800
                )
                Text(
                    text = formattedTime,
                    fontSize = 14.sp,
                    color = Slate400
                )
            }

            // Right: Value (Large font)
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = displayValue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate700
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valueUnit,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Slate400,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

/**
 * Format timestamp to separate date and time strings.
 * Date: "Lun 12 Oct" (Bold)
 * Time: "14:30" (Gray)
 */
private fun formatDateTime(timestamp: Long): Pair<String, String> {
    val locale = Locale.FRENCH
    val date = Date(timestamp)

    val dateFormat = SimpleDateFormat("EEE d MMM", locale)
    val timeFormat = SimpleDateFormat("HH:mm", locale)

    val formattedDate = dateFormat.format(date).replaceFirstChar { it.uppercaseChar() }
    val formattedTime = timeFormat.format(date)

    return Pair(formattedDate, formattedTime)
}
