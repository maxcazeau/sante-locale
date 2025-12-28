package com.santelocale.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.theme.White
import com.santelocale.ui.theme.WhiteAlpha20
import com.santelocale.ui.theme.WhiteAlpha90

/**
 * BigButton component matching the React version:
 * <button className={`${colorClass} w-full p-6 rounded-2xl shadow-lg text-white flex items-center gap-5
 *   transition-transform transform active:scale-95 mb-4`}>
 *   <div className="bg-white/20 p-4 rounded-full">
 *     <Icon size={40} strokeWidth={2.5} />
 *   </div>
 *   <div className="text-left">
 *     <h2 className="text-2xl font-bold">{title}</h2>
 *     <p className="text-white/90 text-sm font-medium mt-1">{subtitle}</p>
 *   </div>
 * </button>
 */
@Composable
fun BigButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 0.dp
        ),
        contentPadding = PaddingValues(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icon container (bg-white/20 p-4 rounded-full)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = WhiteAlpha20,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Text content (text-left)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Title (text-2xl font-bold)
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )

                // Subtitle (text-white/90 text-sm font-medium mt-1)
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = WhiteAlpha90,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
