package com.santelocale.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Colors - Green/Teal harmony palette
val PrimaryGreen = Color(0xFF0E7C66)   // Header background, primary actions
val AccentTeal = Color(0xFF1BA6A6)     // CTA buttons, glucose button, accent
val TealLight = Color(0xFFB2DFDB)      // Teal subtitle tint (updated from 0xFF2DC4C4 for better contrast)
val TealStatus = Color(0xFFE0F2F1)     // Light teal for status pill bg
val TealStatusText = Color(0xFF0E7C66) // Primary green for status text

// Legacy aliases for gradual migration
val Emerald700 = PrimaryGreen          // Header background
val Emerald600 = Color(0xFF059669)     // Food Guide "VERT" category
val Emerald500 = Color(0xFF10B981)     // Status card border
val Emerald800 = Color(0xFF0A5C4D)     // Offline badge (darker primary)

// Teal replaces Blue for glucose/CTAs
val Blue600 = AccentTeal               // Glucose button (now teal)

// Orange - Activity
val Orange50 = Color(0xFFFFF7ED)       // Activity button pressed background
val Orange100 = Color(0xFFFFEDD5)
val Orange500 = Color(0xFFF97316)      // Activity button
val Orange600 = Color(0xFFEA580C)      // Activity button hover

// Yellow - Food Guide "JAUNE" category
val Yellow500 = Color(0xFFEAB308)
val Yellow600 = Color(0xFFCA8A04)

// Red - Food Guide "ROUGE" category
val Red50 = Color(0xFFFEF2F2)
val Red100 = Color(0xFFFEE2E2)
val Red500 = Color(0xFFEF4444)
val Red600 = Color(0xFFDC2626)

// Neutral Colors - Updated background
val Slate50 = Color(0xFFF4F6F7)        // Background (updated from #F8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

// White and backgrounds
val White = Color(0xFFFFFFFF)
val WhiteAlpha20 = Color(0x33FFFFFF)
val WhiteAlpha90 = Color(0xE6FFFFFF)
