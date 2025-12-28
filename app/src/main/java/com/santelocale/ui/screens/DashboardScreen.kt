package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.entity.getGlucoseStatusDisplay
import com.santelocale.ui.components.BigButton
import com.santelocale.ui.components.Header
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.DashboardViewModel

/**
 * Dashboard screen matching the React version:
 * - Header with user name and settings
 * - Status card showing last glucose reading
 * - Three big action buttons (Glucose, Food Guide, Activity)
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header: "Bonjour, {userName}" or "Santé Locale"
        Header(
            title = if (userName.isNotEmpty()) "Bonjour, $userName" else "Santé Locale",
            onSettings = { onNavigate("settings") }
        )

        // Main scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Status Card
            StatusCard(
                lastLog = lastGlucose,
                unit = unit,
                onHistoryClick = { onNavigate("history") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // The Big Three Actions
            BigButton(
                icon = Icons.Default.WaterDrop,  // Droplets -> WaterDrop
                title = "Ma Glycémie",
                subtitle = "Saisir mon taux de sucre",
                backgroundColor = Blue600,
                onClick = { onNavigate("glucose") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BigButton(
                icon = Icons.Default.Restaurant,  // Utensils -> Restaurant
                title = "Guide Alimentaire",
                subtitle = "Bon vs Mauvais",
                backgroundColor = Emerald600,
                onClick = { onNavigate("food") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            BigButton(
                icon = Icons.Default.DirectionsRun,  // Footprints -> DirectionsRun
                title = "Exercice",
                subtitle = "J'ai bougé aujourd'hui",
                backgroundColor = Orange500,
                onClick = { onNavigate("activity") }
            )
        }
    }
}

/**
 * Status Card component matching React version:
 * <div className="bg-white border-l-8 border-emerald-500 rounded-lg shadow-sm p-5 mb-8 flex items-start justify-between">
 */
@Composable
private fun StatusCard(
    lastLog: HealthLog?,
    unit: String,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 8.dp,
                color = Emerald500,
                shape = RoundedCornerShape(8.dp)
            ),
        color = White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Left side: Label and value
            Column {
                // Label
                Text(
                    text = "DERNIÈRE GLYCÉMIE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Value or placeholder
                if (lastLog != null) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Display value (prefer displayValue to keep comma formatting)
                        Text(
                            text = lastLog.displayValue ?: lastLog.value.toString(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Slate800
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = unit,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Status indicator using extension function (matches React logic)
                    Text(
                        text = lastLog.getGlucoseStatusDisplay(unit),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald600
                    )
                } else {
                    // No data placeholder
                    Text(
                        text = "Aucune donnée aujourd'hui",
                        fontSize = 16.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = Slate400
                    )
                }
            }

            // Right side: History button
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = "Historique",
                tint = Emerald600,
                modifier = Modifier
                    .size(32.dp)
                    .clickable(onClick = onHistoryClick)
                    .padding(4.dp)
            )
        }
    }
}
