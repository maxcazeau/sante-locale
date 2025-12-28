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
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.santelocale.data.UserPreferencesRepository
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.screens.ActivityInputScreen
import com.santelocale.ui.screens.DashboardScreen
import com.santelocale.ui.screens.FoodGuideScreen
import com.santelocale.ui.screens.GlucoseInputScreen
import com.santelocale.ui.screens.HistoryScreen
import com.santelocale.ui.screens.SettingsScreen
import com.santelocale.ui.theme.SanteLocaleTheme
import com.santelocale.ui.viewmodel.ActivityViewModel
import com.santelocale.ui.viewmodel.ActivityViewModelFactory
import com.santelocale.ui.viewmodel.DashboardViewModel
import com.santelocale.ui.viewmodel.DashboardViewModelFactory
import com.santelocale.ui.viewmodel.FoodViewModel
import com.santelocale.ui.viewmodel.FoodViewModelFactory
import com.santelocale.ui.viewmodel.GlucoseViewModel
import com.santelocale.ui.viewmodel.GlucoseViewModelFactory
import com.santelocale.ui.viewmodel.HistoryViewModel
import com.santelocale.ui.viewmodel.HistoryViewModelFactory
import com.santelocale.ui.viewmodel.SettingsViewModel
import com.santelocale.ui.viewmodel.SettingsViewModelFactory

// DataStore for user settings (name and unit preference)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Navigation routes for the app.
 */
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Settings : Screen("settings")
    object GlucoseInput : Screen("glucose")
    object FoodGuide : Screen("food")
    object ActivityInput : Screen("activity")
    object History : Screen("history")
}

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
                    // Create UserPreferencesRepository
                    val userPreferencesRepository = UserPreferencesRepository(dataStore)

                    SanteLocaleApp(
                        healthRepository = repository,
                        userPreferencesRepository = userPreferencesRepository
                    )
                }
            }
        }
    }
}

/**
 * Main app composable with Jetpack Navigation.
 */
@Composable
fun SanteLocaleApp(
    healthRepository: HealthRepository,
    userPreferencesRepository: UserPreferencesRepository
) {
    // Navigation controller
    val navController = rememberNavController()

    // User settings from repository
    val userName by userPreferencesRepository.userName.collectAsState(initial = "")
    val unit by userPreferencesRepository.glucoseUnit.collectAsState(initial = "mg/dL")

    // ViewModels
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(healthRepository)
    )

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(userPreferencesRepository, healthRepository)
    )

    val glucoseViewModel: GlucoseViewModel = viewModel(
        factory = GlucoseViewModelFactory(healthRepository)
    )

    val foodViewModel: FoodViewModel = viewModel(
        factory = FoodViewModelFactory(healthRepository)
    )

    val activityViewModel: ActivityViewModel = viewModel(
        factory = ActivityViewModelFactory(healthRepository)
    )

    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(healthRepository)
    )

    // Navigation Host
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = dashboardViewModel,
                userName = userName,
                unit = unit,
                onNavigate = { destination -> navController.navigate(destination) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.GlucoseInput.route) {
            // GlucoseInputScreen with NavController
            GlucoseInputScreen(
                navController = navController,
                viewModel = glucoseViewModel,
                userUnit = unit  // Using real user unit from settings (defaults to "mg/dL")
            )
        }

        composable(Screen.FoodGuide.route) {
            FoodGuideScreen(
                viewModel = foodViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.ActivityInput.route) {
            ActivityInputScreen(
                viewModel = activityViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = historyViewModel,
                unit = unit,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
