package com.santelocale.ui.viewmodel

import com.santelocale.data.entity.FoodItem
import com.santelocale.data.repository.HealthRepository
import com.santelocale.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var repository: HealthRepository

    private lateinit var viewModel: FoodViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock data
        val mockFoods = listOf(
            FoodItem("1", "Lalo", "VERT", "imageUrl", "Good"),
            FoodItem("2", "Riz", "ROUGE", "imageUrl", "Bad"),
            FoodItem("3", "Igname", "JAUNE", "imageUrl", "Okay")
        )
        
        `when`(repository.getFoodsByCategory("VERT")).thenReturn(flowOf(mockFoods.filter { it.category == "VERT" }))
        `when`(repository.getFoodsByCategory("JAUNE")).thenReturn(flowOf(mockFoods.filter { it.category == "JAUNE" }))
        `when`(repository.getFoodsByCategory("ROUGE")).thenReturn(flowOf(mockFoods.filter { it.category == "ROUGE" }))

        viewModel = FoodViewModel(repository)
    }

    @Test
    fun `initial category is VERT`() = runTest {
        val category = viewModel.selectedCategory.first()
        val foods = viewModel.foods.first()
        
        assertEquals("VERT", category)
        assertEquals(1, foods.size)
        assertEquals("Lalo", foods[0].name)
    }

    @Test
    fun `switching category updates food list`() = runTest {
        viewModel.selectCategory("ROUGE")
        
        val category = viewModel.selectedCategory.first()
        val foods = viewModel.foods.first() // This might need a delay or collecting, but flatMapLatest usually emits immediately in tests with proper dispatchers
        
        assertEquals("ROUGE", category)
        assertEquals(1, foods.size)
        assertEquals("Riz", foods[0].name)
    }
}
