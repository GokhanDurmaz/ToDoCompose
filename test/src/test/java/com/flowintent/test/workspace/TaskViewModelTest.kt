/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.test.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: TaskRepository

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(repository.getTasks(any(), any())).thenReturn(flowOf(PagingData.empty<Task>()))
        viewModel = TaskViewModel(repository)
    }

    @Test
    fun `onSearch updates uiState`() = runTest {
        viewModel.onSearch("query")
        assertEquals("query", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onTypeSelected updates uiState`() = runTest {
        viewModel.onTypeSelected(TaskType.WORK)
        assertEquals(TaskType.WORK, viewModel.uiState.value.selectedType)
    }

    @Test
    fun `toggleSelection updates uiState and enables selection mode`() = runTest {
        viewModel.toggleSelection(1)
        assertEquals(true, viewModel.uiState.value.selectedTasks[1])
        assertEquals(true, viewModel.uiState.value.isSelectionMode)
    }
}
