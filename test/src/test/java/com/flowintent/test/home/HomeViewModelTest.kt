/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.home

import com.flowintent.core.db.auth.GetNameUseCase
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.db.model.TaskContent
import com.flowintent.core.db.model.TaskIcon
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.home.ui.vm.HomeViewModel
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.test.scenarios.UseCaseScenarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getHomeCategoriesUseCase: GetHomeCategoriesUseCase

    @Mock
    private lateinit var getNameUseCase: GetNameUseCase

    @Mock
    private lateinit var eventTracker: AppEventTracker

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(getNameUseCase()).thenReturn(flowOf("Test User"))
        whenever(getHomeCategoriesUseCase()).thenReturn(flowOf(Resource.Loading))
    }

    @Test
    fun `loadCategories updates uiState with categories`() = runTest {
        val categories = listOf(
            TaskCategory(
                title = "Gym",
                content = TaskContent("Physical activities"),
                icon = TaskIcon("vector", "FitnessCenter"),
                cardColor = 0xFFE3F2FD,
                iconColor = 0xFF1976D2,
                textColor = 0xFF0D47A1
            )
        )
        whenever(getHomeCategoriesUseCase()).thenReturn(UseCaseScenarios.success(categories))

        viewModel = HomeViewModel(getHomeCategoriesUseCase, getNameUseCase, eventTracker)

        assertEquals(categories, viewModel.uiState.value.categories)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `userName flow updates userName state`() = runTest {
        val expectedName = "John Doe"
        whenever(getNameUseCase()).thenReturn(flowOf(expectedName))

        viewModel = HomeViewModel(getHomeCategoriesUseCase, getNameUseCase, eventTracker)

        // Using backgroundScope to ensure the flow is collected
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userName.collect {}
        }

        assertEquals(expectedName, viewModel.userName.value)
    }
}
