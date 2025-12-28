package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.viewmodel.SettingsViewModel

// Colors
private val Slate100 = Color(0xFFF1F5F9)
private val Slate400 = Color(0xFF94A3B8)
private val Slate600 = Color(0xFF475569)
private val Slate800 = Color(0xFF1E293B)
private val Emerald600 = Color(0xFF059669)
private val Orange500 = Color(0xFFF97316)
private val Blue600 = Color(0xFF2563EB)

/**
 * Settings screen with main navigation menu list.
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    CurvedScreenWrapper(
        title = "Paramètres",
        onBack = onBack
    ) {
        // Single White Card Container
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp)
            ) {
                // Section Title (Optional, for structure)
                Text(
                    text = "GÉNÉRAL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate600,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )

                // Menu Items
                SettingsMenuItem(
                    icon = Icons.Rounded.Person,
                    iconColor = Emerald600,
                    title = "Profil",
                    subtitle = "Modifier vos infos",
                    onClick = { /* Navigate to Profil settings */ }
                )

                SettingsMenuItem(
                    icon = Icons.Rounded.Notifications,
                    iconColor = Orange500,
                    title = "Rappels",
                    subtitle = "Gérer les notifications",
                    onClick = { /* Navigate to Reminders settings */ }
                )

                SettingsMenuItem(
                    icon = Icons.Rounded.Language,
                    iconColor = Blue600,
                    title = "Langue",
                    subtitle = "Français (HT)",
                    onClick = { /* Navigate to Language settings */ }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Additional Section or Info could go here
            }
        }
    }
}

/**
 * Clickable row for a settings menu item.
 */
@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon on the left
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = iconColor.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text in the middle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Slate400,
                fontWeight = FontWeight.Medium
            )
        }

        // Chevron on the right
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(20.dp)
        )
    }
}