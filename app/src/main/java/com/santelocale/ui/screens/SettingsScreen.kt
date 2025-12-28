package com.santelocale.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santelocale.ui.components.Header
import com.santelocale.ui.theme.*
import com.santelocale.ui.viewmodel.SettingsViewModel

// Additional colors for Settings screen
private val Emerald50 = Color(0xFFECFDF5)
private val Red50 = Color(0xFFFEF2F2)
private val Red100 = Color(0xFFFEE2E2)
private val Red500 = Color(0xFFEF4444)

/**
 * Settings screen matching the React SettingsView component.
 * - Text input for user name
 * - Unit selection toggle (mg/dL vs mmol/L)
 * - Save button
 * - Danger zone with clear data button
 */
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userName by viewModel.userName.collectAsState()
    val glucoseUnit by viewModel.glucoseUnit.collectAsState()

    // Local state for editing (allows user to type before saving)
    var nameInput by remember(userName) { mutableStateOf(userName) }
    var selectedUnit by remember(glucoseUnit) { mutableStateOf(glucoseUnit) }

    // Track if there are unsaved changes
    val hasChanges = nameInput != userName || selectedUnit != glucoseUnit

    // Confirmation dialog state
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
    ) {
        // Header with back button
        Header(
            title = "Paramètres",
            onBack = onBack
        )

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Name Input Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "VOTRE NOM",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate600,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    placeholder = {
                        Text(
                            text = "Ex: Papa",
                            color = Slate400
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Emerald500,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Unit Selection Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "UNITÉ DE GLYCÉMIE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate600,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Two-option toggle row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    UnitButton(
                        label = "mg/dL",
                        isSelected = selectedUnit == "mg/dL",
                        onClick = { selectedUnit = "mg/dL" },
                        modifier = Modifier.weight(1f)
                    )

                    UnitButton(
                        label = "mmol/L",
                        isSelected = selectedUnit == "mmol/L",
                        onClick = { selectedUnit = "mmol/L" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "mg/dL est le standard en Haïti. mmol/L est utilisé en Europe/Canada.",
                    fontSize = 12.sp,
                    color = Slate400
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.updateName(nameInput)
                    viewModel.toggleUnit(selectedUnit)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald600
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = "Enregistrer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider
            Divider(
                color = Slate200,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Danger Zone - Clear Data Button
            Button(
                onClick = { showClearDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .border(
                        width = 1.dp,
                        color = Red100,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red50,
                    contentColor = Red500
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Effacer les données",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Confirmation Dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = {
                Text(
                    text = "Effacer les données",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Êtes-vous sûr de vouloir tout effacer ? Cette action est irréversible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showClearDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Red600
                    )
                ) {
                    Text("Effacer", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

/**
 * Unit selection button component.
 * Selected state: emerald border + emerald background + emerald text
 * Unselected state: slate border + white background + slate text
 */
@Composable
private fun UnitButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Emerald50 else White
    val borderColor = if (isSelected) Emerald500 else Slate200
    val textColor = if (isSelected) Emerald700 else Slate400

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
