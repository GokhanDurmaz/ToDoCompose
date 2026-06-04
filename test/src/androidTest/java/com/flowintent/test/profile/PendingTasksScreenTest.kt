/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.profile.ui.PendingTasksScreen
import com.flowintent.profile.ui.vm.PendingTasksUiState
import com.flowintent.profile.ui.vm.PendingTasksViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class PendingTasksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(PendingTasksViewModel::class.java)
    private val uiState = MutableStateFlow(PendingTasksUiState())
    private val tasksFlow = MutableStateFlow(PagingData.empty<Task>())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(uiState)
        whenever(mockViewModel.tasks).thenReturn(tasksFlow)
    }

    @Test
    fun pendingTasksScreen_displaysTitle() {
        composeTestRule.setContent {
            PendingTasksScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Pending Tasks").assertIsDisplayed()
    }

    @Test
    fun pendingTasksScreen_displaysSubtitle() {
        composeTestRule.setContent {
            PendingTasksScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Manage your upcoming schedule").assertIsDisplayed()
    }
}
