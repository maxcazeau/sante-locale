package com.santelocale.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.theme.Emerald700
import com.santelocale.ui.theme.Emerald800
import com.santelocale.ui.theme.Emerald600
import com.santelocale.ui.theme.White

/**
 * Header component matching the React version:
 * <div className="bg-emerald-700 text-white p-4 shadow-md flex items-center justify-between sticky top-0 z-10 shrink-0">
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    title: String,
    onBack: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Emerald700,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Back button + Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                // Back button (ChevronLeft icon from Lucide -> Material Icons)
                if (onBack != null) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Retour",
                        tint = White,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onBack)
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Title
                Text(
                    text = title,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Right side: Offline badge + Settings
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Offline Badge (WifiOff icon)
                Row(
                    modifier = Modifier
                        .background(
                            color = Emerald800,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Using a simple circle as placeholder for WifiOff icon
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(White.copy(alpha = 0.7f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Hors Ligne",
                        color = White.copy(alpha = 0.9f),
                        fontSize = 12.sp
                    )
                }

                // Settings button
                if (onSettings != null) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Paramètres",
                        tint = White,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onSettings)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}
