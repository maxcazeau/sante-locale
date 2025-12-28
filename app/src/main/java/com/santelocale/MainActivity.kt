package com.santelocale

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.screens.DashboardScreen
import com.santelocale.ui.theme.SanteLocaleTheme
import com.santelocale.ui.viewmodel.DashboardViewModel
import com.santelocale.ui.viewmodel.DashboardViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// DataStore for user settings (name and unit preference)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = HealthDatabase.getDatabase(applicationContext)
        val repository = HealthRepository(
            healthLogDao = database.healthLogDao(),
            foodItemDao = database.foodItemDao()
        )

        setContent {
            SanteLocaleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SanteLocaleApp(
                        repository = repository,
                        dataStore = dataStore
                    )
                }
            }
        }
    }
}

/**
 * Main app composable with navigation state management.
 * Matches React's view state management: dashboard, glucose, food, activity, history, settings
 */
@Composable
fun SanteLocaleApp(
    repository: HealthRepository,
    dataStore: DataStore<Preferences>
) {
    // Navigation state
    var currentScreen by remember { mutableStateOf("dashboard") }

    // User settings
    val userName by dataStore.data.map { prefs ->
        prefs[stringPreferencesKey("user_name")] ?: ""
    }.collectAsState(initial = "")

    val unit by dataStore.data.map { prefs ->
        prefs[stringPreferencesKey("unit")] ?: "mg/dL"
    }.collectAsState(initial = "mg/dL")

    // ViewModel
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(repository)
    )

    // Navigation function
    val navigate: (String) -> Unit = { destination ->
        currentScreen = destination
    }

    // Render current screen based on navigation state
    when (currentScreen) {
        "dashboard" -> {
            DashboardScreen(
                viewModel = viewModel,
                userName = userName,
                unit = unit,
                onNavigate = navigate
            )
        }
        "settings" -> {
            // TODO: Implement SettingsScreen
            // For now, show placeholder
            PlaceholderScreen(
                screenName = "Settings",
                onBack = { navigate("dashboard") }
            )
        }
        "glucose" -> {
            // TODO: Implement GlucoseInputScreen
            PlaceholderScreen(
                screenName = "Glucose Input",
                onBack = { navigate("dashboard") }
            )
        }
        "food" -> {
            // TODO: Implement FoodGuideScreen
            PlaceholderScreen(
                screenName = "Food Guide",
                onBack = { navigate("dashboard") }
            )
        }
        "activity" -> {
            // TODO: Implement ActivityInputScreen
            PlaceholderScreen(
                screenName = "Activity Input",
                onBack = { navigate("dashboard") }
            )
        }
        "history" -> {
            // TODO: Implement HistoryScreen
            PlaceholderScreen(
                screenName = "History",
                onBack = { navigate("dashboard") }
            )
        }
    }
}

/**
 * Temporary placeholder for unimplemented screens
 */
@Composable
fun PlaceholderScreen(
    screenName: String,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            androidx.compose.material3.Text(
                text = "$screenName Screen",
                style = MaterialTheme.typography.titleLarge
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.Button(onClick = onBack) {
                androidx.compose.material3.Text("Retour")
            }
        }
    }
}
