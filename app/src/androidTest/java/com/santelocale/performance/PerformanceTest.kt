package com.santelocale.performance

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.entity.HealthLog
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.screens.HistoryScreen
import com.santelocale.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.system.measureTimeMillis

@RunWith(AndroidJUnit4::class)
class PerformanceTest {

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
    fun testHistoryScrollPerformanceWith1000Logs() {
        // 1. Pre-populate 1000 logs
        runBlocking {
            val logs = (1..1000).map { i ->
                HealthLog(
                    type = if (i % 2 == 0) "GLUCOSE" else "ACTIVITY",
                    value = 100f + i,
                    displayValue = (100 + i).toString(),
                    label = if (i % 2 == 0) "mg/dL" else "Marche",
                    date = System.currentTimeMillis() - (i * 1000 * 60 * 10) // Spread out over time
                )
            }
            logs.forEach { repository.insertLog(it) }
        }

        // 2. Measure composition and initial render time
        val renderTime = measureTimeMillis {
            composeTestRule.setContent {
                HistoryScreen(
                    viewModel = viewModel,
                    unit = "mg/dL",
                    onBack = {}
                )
            }
            composeTestRule.waitForIdle()
        }

        android.util.Log.d("PerformanceTest", "Rendered 1000 logs in $renderTime ms")

        // 3. Wait for data to load from Room Flow, then verify visibility
        // i=1 has displayValue="101", which should be at the top of the list (sorted by date descending)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("101").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("101").assertIsDisplayed()

        // 4. Perform a scroll to middle/end to check for jank (indirectly)
        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(500)
        composeTestRule.waitForIdle()
        
        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(999)
        composeTestRule.waitForIdle()
        
        // Final item should be visible (i=1000 has displayValue="1100", oldest date, at bottom)
        composeTestRule.onNodeWithText("1100").assertIsDisplayed()
    }
}
