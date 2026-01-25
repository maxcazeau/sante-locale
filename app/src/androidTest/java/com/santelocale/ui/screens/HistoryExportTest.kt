package com.santelocale.ui.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryExportTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: HealthDatabase
    private lateinit var viewModel: HistoryViewModel
    private lateinit var repository: HealthRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HealthDatabase::class.java
        ).build()
        
        repository = HealthRepository(db.healthLogDao(), db.foodItemDao())
        viewModel = HistoryViewModel(repository)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testHistoryDisplayAndPdfButtonPresence() {
        // Add some logs
        runBlocking {
            repository.insertLog(HealthLog(type = "GLUCOSE", value = 120f, displayValue = "120", label = "mg/dL", date = System.currentTimeMillis()))
        }

        composeTestRule.setContent {
            HistoryScreen(
                viewModel = viewModel,
                unit = "mg/dL",
                onBack = {}
            )
        }

        // Verify the log is displayed
        composeTestRule.onNodeWithText("120").assertIsDisplayed()

        // Verify PDF Share FAB is present (using content description if available, otherwise icon/type check)
        // Since we used "Exporter PDF" as content description in FAB
        composeTestRule.onNodeWithContentDescription("Exporter PDF").assertIsDisplayed()
    }

    @Test
    fun testHistoryEmptyState() {
        composeTestRule.setContent {
            HistoryScreen(
                viewModel = viewModel,
                unit = "mg/dL",
                onBack = {}
            )
        }

        // Verify "Aucun historique" text
        composeTestRule.onNodeWithText("Aucun historique").assertIsDisplayed()
        
        // Verify FAB is NOT present
        composeTestRule.onNodeWithContentDescription("Exporter PDF").assertDoesNotExist()
    }
}
