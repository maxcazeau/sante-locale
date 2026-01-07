package com.santelocale.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.navigation.NavController
import com.santelocale.R
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.GlucoseViewModel

/**
 * Glucose Input screen with manual entry keypad and context toggles.
 */
@Composable
fun GlucoseInputScreen(
    navController: NavController,
    viewModel: GlucoseViewModel,
    userUnit: String,
    modifier: Modifier = Modifier
) {
    val inputValue by viewModel.inputValue.collectAsState()
    val errorKey by viewModel.error.collectAsState()
    var selectedContext by remember { mutableStateOf<String?>(null) } // "Avant repas" or "Après repas"
    
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Keypad items
    val keypadItems = remember { listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ",", "0", "⌫") }

    // Effect to show snackbar when error changes
    LaunchedEffect(errorKey) {
        errorKey?.let { key ->
            val message = when (key) {
                "error_invalid_value" -> context.getString(R.string.error_invalid_value)
                "error_value_too_low" -> context.getString(R.string.error_value_too_low)
                "error_value_too_high" -> context.getString(R.string.error_value_too_high)
                else -> key
            }
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent, // Let CurvedScreenWrapper handle background
        modifier = modifier
    ) { padding ->
        CurvedScreenWrapper(
            title = stringResource(R.string.title_new_glucose),
            onBack = { navController.popBackStack() },
            modifier = Modifier.padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Main Display Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Label: 'VOTRE TAUX'
                        Text(
                            text = stringResource(R.string.label_your_rate),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Slate400
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Value Display
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Value Text
                            Text(
                                text = inputValue.ifEmpty { "--" },
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Black,
                                color = if (inputValue.isNotEmpty()) Slate800 else Slate300
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            // Unit
                            Text(
                                text = userUnit,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate400,
                                modifier = Modifier.padding(bottom = 14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Context Toggles (Avant / Après repas)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ContextToggleButton(
                                text = stringResource(R.string.context_before_meal),
                                isSelected = selectedContext == "Avant repas",
                                onClick = { selectedContext = if (selectedContext == "Avant repas") null else "Avant repas" },
                                modifier = Modifier.weight(1f)
                            )

                            ContextToggleButton(
                                text = stringResource(R.string.context_after_meal),
                                isSelected = selectedContext == "Après repas",
                                onClick = { selectedContext = if (selectedContext == "Après repas") null else "Après repas" },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Keypad Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(keypadItems) { item ->
                        KeypadButton(
                            text = item,
                            isDelete = item == "⌫",
                            onClick = {
                                when (item) {
                                    "⌫" -> viewModel.deleteLastDigit()
                                    "," -> viewModel.appendDecimal()
                                    else -> viewModel.appendDigit(item)
                                }
                            }
                        )
                    }
                }

                // Save Button
                Button(
                    onClick = {
                        if (viewModel.saveGlucose(userUnit, selectedContext)) {
                            navController.popBackStack()
                        }
                    },
                    enabled = inputValue.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Slate300
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = if (inputValue.isNotEmpty()) {
                                    Brush.horizontalGradient(
                                        colors = listOf(AccentTeal, PrimaryGreen)
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(Slate300, Slate300)
                                    )
                                },
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.btn_save),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

/**
 * Context Toggle Button (Avant/Après repas).
 * Emerald theme when selected, Light Gray theme when unselected.
 */
@Composable
private fun ContextToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick)
            .alpha(if (isSelected) 1f else 0.8f),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) TealLight else Color(0xFFF5F5F5),
        border = if (isSelected) {
            BorderStroke(1.5.dp, AccentTeal)
        } else {
            BorderStroke(1.dp, Color(0xFFE0E0E0))
        },
        shadowElevation = 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) PrimaryGreen else Slate400,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Large, touch-friendly keypad button.
 */
@Composable
private fun KeypadButton(
    text: String,
    isDelete: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isDelete) Red50 else Color.White

    Surface(
        modifier = modifier
            .height(64.dp) // Large touch target
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isDelete) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Backspace,
                    contentDescription = stringResource(R.string.cd_delete_digit),
                    tint = Red500,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(
                    text = text,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate700,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
