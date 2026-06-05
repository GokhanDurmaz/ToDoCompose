/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.workspace.ui.ToDoListScreen
import com.flowintent.workspace.ui.vm.TaskUiState
import com.flowintent.workspace.ui.vm.TaskViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ToDoTaskListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(TaskViewModel::class.java)
    private val uiState = MutableStateFlow(TaskUiState())
    private val tasksFlow = MutableStateFlow(PagingData.empty<Task>())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(uiState)
        whenever(mockViewModel.tasks).thenReturn(tasksFlow)
    }

    @Test
    fun toDoListScreen_displaysTaskDetailsTitle() {
        composeTestRule.setContent {
            ToDoListScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Task Details").assertIsDisplayed()
    }

    @Test
    fun toDoListScreen_displaysEmptyPlaceholder_whenNoTasks() {
        composeTestRule.setContent {
            ToDoListScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("No tasks yet.").assertIsDisplayed()
    }
}
