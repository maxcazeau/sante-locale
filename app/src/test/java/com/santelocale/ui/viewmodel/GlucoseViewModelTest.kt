package com.santelocale.ui.viewmodel

import com.santelocale.data.repository.HealthRepository
import com.santelocale.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class GlucoseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var repository: HealthRepository

    private lateinit var viewModel: GlucoseViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = GlucoseViewModel(repository)
    }

    @Test
    fun `initial state is empty`() = runTest {
        val state = viewModel.inputValue.first()
        assertEquals("", state)
    }

    @Test
    fun `appendDigit adds digit and formats correctly`() = runTest {
        viewModel.appendDigit("1")
        assertEquals("1", viewModel.inputValue.first())

        viewModel.appendDigit("2")
        assertEquals("12", viewModel.inputValue.first())
    }

    @Test
    fun `appendDecimal adds comma`() = runTest {
        viewModel.appendDigit("5")
        viewModel.appendDecimal()
        assertEquals("5,", viewModel.inputValue.first())
        
        // Ensure double decimal is prevented
        viewModel.appendDecimal()
        assertEquals("5,", viewModel.inputValue.first())
    }

    @Test
    fun `saveGlucose calls repository`() = runTest {
        // Simulating input "100"
        viewModel.appendDigit("1")
        viewModel.appendDigit("0")
        viewModel.appendDigit("0")
        
        viewModel.saveGlucose("mg/dL")
        
        // Verify repository save was called
        verify(repository).insertLog(any())
        
        // Check input is cleared after save
        assertEquals("", viewModel.inputValue.first())
    }
    
    @Test
    fun `clearInput resets value`() = runTest {
        viewModel.appendDigit("9")
        viewModel.clearInput()
        assertEquals("", viewModel.inputValue.first())
    }
}
