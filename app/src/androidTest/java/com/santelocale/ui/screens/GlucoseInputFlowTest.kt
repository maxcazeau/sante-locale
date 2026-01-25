package com.santelocale.ui.screens

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.repository.HealthRepository
import com.santelocale.ui.viewmodel.GlucoseViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlucoseInputFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var db: HealthDatabase
    private lateinit var viewModel: GlucoseViewModel
    private lateinit var repository: HealthRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HealthDatabase::class.java
        ).build()
        
        repository = HealthRepository(db.healthLogDao(), db.foodItemDao())
        viewModel = GlucoseViewModel(repository)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testValidGlucoseInputAndSave() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GlucoseInputScreen(
                navController = navController,
                viewModel = viewModel,
                userUnit = "mg/dL"
            )
        }

        // Enter "120"
        composeTestRule.onNodeWithText("1").performClick()
        composeTestRule.onNodeWithText("2").performClick()
        composeTestRule.onNodeWithText("0").performClick()

        // Verify display shows 120
        composeTestRule.onNodeWithText("120").assertIsDisplayed()

        // Click Save (ENREGISTRER)
        composeTestRule.onNodeWithText("ENREGISTRER").performClick()

        // Wait for potential navigation or clear (though in this test we stay on screen if it was valid but we want to check state)
        composeTestRule.waitForIdle()
        
        // Input should be cleared in viewModel
        assert(viewModel.inputValue.value == "")
    }

    @Test
    fun testInvalidLowGlucoseShowsError() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GlucoseInputScreen(
                navController = navController,
                viewModel = viewModel,
                userUnit = "mg/dL"
            )
        }

        // Enter "5" (too low for mg/dL, min is 20)
        composeTestRule.onNodeWithText("5").performClick()

        // Click Save
        composeTestRule.onNodeWithText("ENREGISTRER").performClick()

        // Check if Snackbar with error message appears
        // The snackbar might take a moment to appear
        composeTestRule.waitForIdle()
        
        val context = ApplicationProvider.getApplicationContext<Context>()
        val expectedError = context.getString(com.santelocale.R.string.error_value_too_low)
        
        // Use useUnmergedTree = true if needed, but normally snackbars are findable by text
        composeTestRule.onNodeWithText(expectedError).assertIsDisplayed()
    }

    @Test
    fun testInvalidHighGlucoseShowsError() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            GlucoseInputScreen(
                navController = navController,
                viewModel = viewModel,
                userUnit = "mg/dL"
            )
        }

        // Enter "700" (too high, max is 600)
        composeTestRule.onNodeWithText("7").performClick()
        composeTestRule.onNodeWithText("0").performClick()
        composeTestRule.onNodeWithText("0").performClick()

        // Click Save
        composeTestRule.onNodeWithText("ENREGISTRER").performClick()

        val context = ApplicationProvider.getApplicationContext<Context>()
        val expectedError = context.getString(com.santelocale.R.string.error_value_too_high)
        
        composeTestRule.onNodeWithText(expectedError).assertIsDisplayed()
    }
}
