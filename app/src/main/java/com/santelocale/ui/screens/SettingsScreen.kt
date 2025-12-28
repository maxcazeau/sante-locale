package com.santelocale.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.santelocale.ui.components.CurvedScreenWrapper
import com.santelocale.ui.viewmodel.SettingsViewModel
import com.santelocale.utils.ReminderScheduler

// Colors
private val Slate50 = Color(0xFFF8FAFC)
private val Slate100 = Color(0xFFF1F5F9)
private val Slate300 = Color(0xFFCBD5E1)
private val Slate400 = Color(0xFF94A3B8)
private val Slate600 = Color(0xFF475569)
private val Slate700 = Color(0xFF334155)
private val Slate800 = Color(0xFF1E293B)
private val Emerald500 = Color(0xFF10B981)
private val Emerald600 = Color(0xFF059669)
private val Orange500 = Color(0xFFF97316)
private val Blue600 = Color(0xFF2563EB)
private val Red50 = Color(0xFFFEF2F2)
private val Red100 = Color(0xFFFEE2E2)
private val Red500 = Color(0xFFEF4444)

// Internal Routes
private enum class SettingsRoute {
    Menu,
    Account,
    Notifications,
    Privacy,
    Language
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf(SettingsRoute.Menu) }

    // Handle back press for internal navigation
    val handleBack = {
        if (currentRoute == SettingsRoute.Menu) {
            onBack()
        } else {
            currentRoute = SettingsRoute.Menu
        }
    }

    CurvedScreenWrapper(
        title = when (currentRoute) {
            SettingsRoute.Menu -> "Paramètres"
            SettingsRoute.Account -> "Profil"
            SettingsRoute.Notifications -> "Rappels"
            SettingsRoute.Privacy -> "Confidentialité"
            SettingsRoute.Language -> "Langue"
        },
        onBack = handleBack
    ) {
        // Main Content Card
        Surface(
            modifier = Modifier.fillMaxWidth(), // Removed fillMaxSize, allows wrapping
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            AnimatedContent(
                targetState = currentRoute,
                transitionSpec = {
                    if (targetState != SettingsRoute.Menu) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "SettingsNavigation"
            ) { route ->
                when (route) {
                    SettingsRoute.Menu -> SettingsMenu(
                        onNavigate = { currentRoute = it }
                    )
                    SettingsRoute.Account -> AccountSettings(
                        viewModel = viewModel,
                        onBack = handleBack
                    )
                    SettingsRoute.Notifications -> NotificationSettings(
                        viewModel = viewModel,
                        onBack = handleBack
                    )
                    SettingsRoute.Privacy -> PrivacySettings(
                        viewModel = viewModel,
                        onBack = handleBack
                    )
                    SettingsRoute.Language -> LanguageSettings(
                        onBack = handleBack
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsMenu(
    onNavigate: (SettingsRoute) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp)
    ) {
        SectionTitle("GÉNÉRAL")

        SettingsMenuItem(
            icon = Icons.Rounded.Person,
            iconColor = Emerald600,
            title = "Profil & Compte",
            subtitle = "Nom, Unité de glycémie",
            onClick = { onNavigate(SettingsRoute.Account) }
        )

        SettingsMenuItem(
            icon = Icons.Rounded.Notifications,
            iconColor = Orange500,
            title = "Notifications",
            subtitle = "Rappels quotidiens",
            onClick = { onNavigate(SettingsRoute.Notifications) }
        )

        SettingsMenuItem(
            icon = Icons.Rounded.Language,
            iconColor = Blue600,
            title = "Langue",
            subtitle = "Français (HT)",
            onClick = { onNavigate(SettingsRoute.Language) }
        )

        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle("SÉCURITÉ")

        SettingsMenuItem(
            icon = Icons.Rounded.Lock,
            iconColor = Red500,
            title = "Confidentialité & Données",
            subtitle = "Gérer vos données",
            onClick = { onNavigate(SettingsRoute.Privacy) }
        )
    }
}

@Composable
private fun AccountSettings(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val glucoseUnit by viewModel.glucoseUnit.collectAsState()
    
    var nameInput by remember(userName) { mutableStateOf(userName) }
    var selectedUnit by remember(glucoseUnit) { mutableStateOf(glucoseUnit) }

    Column(
        modifier = Modifier
            .fillMaxWidth() // Wraps content vertically
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Spacing handled here
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InputLabel("VOTRE NOM")
            
            TextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ex: Papa", color = Slate400) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Slate50,
                    focusedContainerColor = Slate50,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Emerald600
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InputLabel("UNITÉ DE GLYCÉMIE")
            
            UnitSwitcher(
                options = listOf("mg/dL", "mmol/L"),
                selectedOption = selectedUnit,
                onOptionSelected = { selectedUnit = it }
            )
            
            Text(
                text = "mg/dL est le standard en Haïti.",
                fontSize = 12.sp,
                color = Slate400
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Extra visual separation before button

        PrimaryButton(
            text = "Enregistrer",
            onClick = {
                viewModel.updateName(nameInput)
                viewModel.toggleUnit(selectedUnit)
                onBack()
            }
        )
    }
}

@Composable
private fun NotificationSettings(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val morningReminder by viewModel.morningReminder.collectAsState()
    val eveningReminder by viewModel.eveningReminder.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* No-op, just requesting */ }
    )

    fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        InputLabel("RAPPELS QUOTIDIENS")
        
        Column {
            SwitchRow(
                title = "Matin (A jeun)",
                subtitle = "07:00 AM",
                checked = morningReminder,
                onCheckedChange = { enabled ->
                    viewModel.setMorningReminder(enabled)
                    if (enabled) {
                        checkAndRequestPermission()
                        ReminderScheduler.scheduleReminder(context, 7, 0, "morning_reminder", "Bonjour !", "N'oubliez pas votre glycémie à jeun.")
                    } else {
                        ReminderScheduler.cancelReminder(context, "morning_reminder")
                    }
                }
            )
            
            Divider(color = Slate100, thickness = 1.dp)
            
            SwitchRow(
                title = "Soir (Après repas)",
                subtitle = "08:00 PM",
                checked = eveningReminder,
                onCheckedChange = { enabled ->
                    viewModel.setEveningReminder(enabled)
                    if (enabled) {
                        checkAndRequestPermission()
                        ReminderScheduler.scheduleReminder(context, 20, 0, "evening_reminder", "Bonsoir !", "Avez-vous vérifié votre glycémie après le repas ?")
                    } else {
                        ReminderScheduler.cancelReminder(context, "evening_reminder")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Retour",
            onClick = onBack
        )
    }
}

@Composable
private fun PrivacySettings(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Text(
                text = "Gestion des données",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toutes vos données sont stockées localement sur votre téléphone.",
                fontSize = 14.sp,
                color = Slate600,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InputLabel("ZONE DANGER", color = Red500)
            
            Button(
                onClick = { showClearDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red50,
                    contentColor = Red500
                ),
                border = BorderStroke(1.dp, Red100),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Effacer toutes les données", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        // Confirmation Dialog
        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Attention", fontWeight = FontWeight.Bold) },
                text = { Text("Voulez-vous vraiment effacer tout l'historique ? Cette action est irréversible.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearAllData()
                            showClearDialog = false
                            onBack()
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Red500)
                    ) {
                        Text("Effacer", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) { Text("Annuler") }
                }
            )
        }
    }
}

@Composable
private fun LanguageSettings(
    onBack: () -> Unit
) {
    val languages = listOf("Français (HT)", "Kreyòl Ayisyen", "English")
    var selectedLanguage by remember { mutableStateOf("Français (HT)") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InputLabel("LANGUE DE L'APPLICATION")

        Column {
            languages.forEach { language ->
                val isSelected = language == selectedLanguage
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { selectedLanguage = language }
                        .semantics {
                            role = Role.RadioButton
                            selected = isSelected
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { selectedLanguage = language },
                        colors = RadioButtonDefaults.colors(selectedColor = Emerald600)
                    )
                    Text(
                        text = language,
                        fontSize = 16.sp,
                        color = Slate800,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider(color = Slate100)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Enregistrer",
            onClick = onBack
        )
    }
}

// --- Reusable Components ---

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Slate600,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
private fun InputLabel(text: String, color: Color = Slate600) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

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
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .semantics {
                role = Role.Button
                contentDescription = "Ouvrir les paramètres de $title"
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = iconColor.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null, // Decorative, row has description
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                subtitle,
                fontSize = 13.sp,
                color = Slate400,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Slate400,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .semantics {
                stateDescription = if (checked) "Activé" else "Désactivé"
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Slate800,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                subtitle,
                fontSize = 14.sp,
                color = Slate400,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Emerald600,
                uncheckedThumbColor = Slate400,
                uncheckedTrackColor = Slate300
            )
        )
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Emerald600),
        elevation = ButtonDefaults.buttonElevation(2.dp)
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun UnitSwitcher(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .background(Slate100)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                if (isSelected) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onOptionSelected(option) }
                            .semantics {
                                role = Role.RadioButton
                                selected = true
                                contentDescription = "Unité $option sélectionnée"
                            },
                        shape = RoundedCornerShape(50),
                        color = Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(option, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate700)
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .clickable { onOptionSelected(option) }
                            .semantics {
                                role = Role.RadioButton
                                selected = false
                                contentDescription = "Sélectionner unité $option"
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(option, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Slate400)
                    }
                }
            }
        }
    }
}