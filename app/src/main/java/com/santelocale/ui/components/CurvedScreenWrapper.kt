package com.santelocale.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.theme.HeaderContentOverlap
import com.santelocale.ui.theme.HeaderContentTopPadding
import com.santelocale.ui.theme.HeaderCornerRadius
import com.santelocale.ui.theme.HeaderHeight

// Colors - New teal/green harmony palette
private val PrimaryGreen = Color(0xFF0E7C66)
private val AccentTeal = Color(0xFF1BA6A6)
private val Slate100 = Color(0xFFF4F6F7)

/**
 * Reusable screen wrapper with curved emerald header and floating content.
 *
 * Structure:
 * - Root: Slate100 background (so cards appear to float)
 * - Header: Emerald gradient with 32dp rounded bottom corners
 * - Content: Floats over header with -48dp offset
 *
 * @param title Screen title displayed in the header
 * @param onBack Callback when back button is pressed
 * @param content Content that floats over the curved header
 */
@Composable
fun CurvedScreenWrapper(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Root Column with Slate100 background - CRITICAL for floating effect
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate100) // Must be Slate100, NOT white
    ) {
        // 1. Curved Header (drawn first, appears behind content)
        // Uses unified HeaderHeight for seamless screen transitions
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderHeight)
                .clip(RoundedCornerShape(bottomStart = HeaderCornerRadius, bottomEnd = HeaderCornerRadius))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(AccentTeal, PrimaryGreen)
                    )
                )
        ) {
            // Header Content: Back button + Title
            // Uses HeaderContentTopPadding for consistent positioning across screens
            Row(
                modifier = Modifier
                    .padding(top = HeaderContentTopPadding, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button - Semi-transparent white circle with white arrow
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Retour", // localized in strings.xml ideally
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 2. Floating Content (drawn after header, appears on top)
        // Uses HeaderContentOverlap to pull content UP to overlap the header
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = -HeaderContentOverlap)
                .padding(horizontal = 20.dp),
            content = content
        )
    }
}
