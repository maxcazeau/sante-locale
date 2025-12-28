package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.GlucoseViewModel

// Colors for keypad
private val Red500 = Color(0xFFEF4444)

/**
 * Glucose Input screen with custom numeric keypad.
 * Uses comma (,) for decimal separator (French formatting).
 *
 * @param navController Navigation controller for back navigation
 * @param viewModel GlucoseViewModel managing input state
 * @param userUnit The user's preferred glucose unit (e.g., "mg/dL" or "mmol/L")
 */
@Composable
fun GlucoseInputScreen(
    navController: NavController,
    viewModel: GlucoseViewModel,
    userUnit: String
) {
    val inputValue by viewModel.inputValue.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header with Back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Emerald700)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Retour",
                tint = White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Nouvelle Glycémie",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        // Main content area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display Card - shows current input value
            DisplayCard(
                value = inputValue,
                unit = userUnit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Keypad - 3x4 grid
            Keypad(
                onDigit = { viewModel.appendDigit(it) },
                onDecimal = { viewModel.appendDecimal() },
                onDelete = { viewModel.deleteLastDigit() },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            SaveButton(
                enabled = inputValue.isNotEmpty(),
                onClick = {
                    viewModel.saveGlucose(userUnit)
                    navController.popBackStack()
                }
            )
        }
    }
}

/**
 * Display card showing the current input value in large text.
 */
@Composable
private fun DisplayCard(
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                // Large value display (60sp as requested)
                Text(
                    text = value.ifEmpty { "--" },
                    fontSize = 60.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (value.isNotEmpty()) Slate800 else Slate300
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Unit display
                Text(
                    text = unit,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate500,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}

/**
 * Custom numeric keypad with 3x4 grid layout.
 * Row 1: 1, 2, 3
 * Row 2: 4, 5, 6
 * Row 3: 7, 8, 9
 * Row 4: comma, 0, delete
 */
@Composable
private fun Keypad(
    onDigit: (String) -> Unit,
    onDecimal: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: 1, 2, 3
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeypadButton("1", onClick = { onDigit("1") }, modifier = Modifier.weight(1f))
            KeypadButton("2", onClick = { onDigit("2") }, modifier = Modifier.weight(1f))
            KeypadButton("3", onClick = { onDigit("3") }, modifier = Modifier.weight(1f))
        }

        // Row 2: 4, 5, 6
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeypadButton("4", onClick = { onDigit("4") }, modifier = Modifier.weight(1f))
            KeypadButton("5", onClick = { onDigit("5") }, modifier = Modifier.weight(1f))
            KeypadButton("6", onClick = { onDigit("6") }, modifier = Modifier.weight(1f))
        }

        // Row 3: 7, 8, 9
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeypadButton("7", onClick = { onDigit("7") }, modifier = Modifier.weight(1f))
            KeypadButton("8", onClick = { onDigit("8") }, modifier = Modifier.weight(1f))
            KeypadButton("9", onClick = { onDigit("9") }, modifier = Modifier.weight(1f))
        }

        // Row 4: comma, 0, delete
        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Decimal (comma) button
            KeypadButton(
                text = ",",
                onClick = onDecimal,
                backgroundColor = Slate100,
                modifier = Modifier.weight(1f)
            )

            // Zero button
            KeypadButton("0", onClick = { onDigit("0") }, modifier = Modifier.weight(1f))

            // Delete button
            KeypadDeleteButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Individual keypad button with press animation.
 */
@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = White,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val verticalOffset = if (isPressed) 4.dp else 0.dp
    val bottomBorderWidth = if (isPressed) 0.dp else 4.dp

    Box(
        modifier = modifier
            .fillMaxHeight()
            .offset(y = verticalOffset)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .then(
                if (!isPressed) {
                    Modifier.border(
                        width = bottomBorderWidth,
                        color = Slate200,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Slate800
        )
    }
}

/**
 * Delete button with backspace icon in red.
 */
@Composable
private fun KeypadDeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val verticalOffset = if (isPressed) 4.dp else 0.dp
    val bottomBorderWidth = if (isPressed) 0.dp else 4.dp

    Box(
        modifier = modifier
            .fillMaxHeight()
            .offset(y = verticalOffset)
            .clip(RoundedCornerShape(12.dp))
            .background(Slate100)
            .then(
                if (!isPressed) {
                    Modifier.border(
                        width = bottomBorderWidth,
                        color = Slate200,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else Modifier
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Backspace,
            contentDescription = "Supprimer",
            tint = Red500,
            modifier = Modifier.size(28.dp)
        )
    }
}

/**
 * Save button - Emerald when enabled, Gray when disabled.
 */
@Composable
private fun SaveButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Emerald600,
            disabledContainerColor = Slate300
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        )
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "ENREGISTRER",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
