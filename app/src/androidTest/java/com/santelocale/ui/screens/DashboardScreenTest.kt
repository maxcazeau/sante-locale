package com.santelocale.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.viewmodel.DashboardViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: HealthDatabase
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HealthDatabase::class.java
        ).build()
        
        val repository = HealthRepository(db.healthLogDao(), db.foodItemDao())
        viewModel = DashboardViewModel(repository)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun dashboardShowsQuickActions() {
        // Start the app (or just the DashboardScreen)
        composeTestRule.setContent {
            DashboardScreen(
                viewModel = viewModel,
                userName = "Test User",
                unit = "mg/dL",
                onNavigate = {}
            )
        }

        // Verify "Actions Rapides" header is visible
        composeTestRule.onNodeWithText("Actions Rapides").assertIsDisplayed()
        
        // Verify Buttons exist
        composeTestRule.onNodeWithText("Saisir mon taux").assertIsDisplayed()
        composeTestRule.onNodeWithText("Manger").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bouger").assertIsDisplayed()
        
        // Verify User Name
        composeTestRule.onNodeWithText("Bonjour, Test User 👋").assertIsDisplayed()
    }
}
