package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WaterDrop
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
import com.santelocale.ui.components.Header
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.HistoryViewModel
import com.santelocale.utils.PdfGenerator
import java.text.SimpleDateFormat
import java.util.*

// Additional colors for history items
private val Blue100 = Color(0xFFDBEAFE)
private val Orange100 = Color(0xFFFFEDD5)

/**
 * History screen showing all health logs.
 * Matches the React HistoryView component design.
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Slate50,
        floatingActionButton = {
            // Only show FAB if there are logs to export
            if (logs.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        PdfGenerator.generateAndShare(
                            context = context,
                            userName = userName,
                            logs = logs,
                            unit = unit
                        )
                    },
                    containerColor = Emerald600,
                    contentColor = White
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Exporter PDF"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Header(
                title = "Historique",
                onBack = onBack
            )

            // Content
            if (logs.isEmpty()) {
                // Empty state
                EmptyState()
            } else {
                // Log list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(logs, key = { it.id }) { log ->
                        HistoryItem(
                            log = log,
                            unit = unit
                        )
                    }
                }
            }
        }
    }
}

/**
 * Empty state when no logs are available.
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = Slate400.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aucun historique disponible.",
                fontSize = 16.sp,
                color = Slate400,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Individual history log item.
 * Shows icon, title, date, and value.
 */
@Composable
private fun HistoryItem(
    log: HealthLog,
    unit: String,
    modifier: Modifier = Modifier
) {
    val isGlucose = log.type == "GLUCOSE"

    // Colors based on type
    val iconBackground = if (isGlucose) Blue100 else Orange100
    val iconTint = if (isGlucose) Blue600 else Orange500

    // Title text
    val title = if (isGlucose) "Glycémie" else (log.label ?: "Activité")

    // Value display
    val displayValue = if (isGlucose) {
        log.displayValue ?: log.value.toInt().toString()
    } else {
        log.value.toInt().toString()
    }
    val valueUnit = if (isGlucose) unit else "min"

    // Format date: "Lun, 12 Oct - 14:30"
    val formattedDate = formatDate(log.date)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Icon + Info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon with colored background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBackground, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isGlucose) Icons.Default.WaterDrop else Icons.Default.DirectionsRun,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and date
                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = Slate500
                    )
                }
            }

            // Right side: Value
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = displayValue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Slate700
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valueUnit,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

/**
 * Format timestamp to French date string.
 * Example: "Lun, 12 oct. - 14:30"
 */
private fun formatDate(timestamp: Long): String {
    val locale = Locale.FRENCH
    val dateFormat = SimpleDateFormat("EEE, d MMM - HH:mm", locale)
    return dateFormat.format(Date(timestamp))
        .replaceFirstChar { it.uppercaseChar() } // Capitalize first letter
}
