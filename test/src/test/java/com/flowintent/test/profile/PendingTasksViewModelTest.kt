/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.profile

import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.task.GetTasksUseCase
import com.flowintent.profile.ui.vm.PendingTasksViewModel
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
class PendingTasksViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getTasksUseCase: GetTasksUseCase

    private lateinit var viewModel: PendingTasksViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(getTasksUseCase(any(), any())).thenReturn(flowOf(PagingData.empty<Task>()))
        viewModel = PendingTasksViewModel(getTasksUseCase)
    }

    @Test
    fun `onSearchQueryChange updates uiState`() = runTest {
        viewModel.onSearchQueryChange("test query")
        assertEquals("test query", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onTypeSelected updates uiState`() = runTest {
        viewModel.onTypeSelected(TaskType.WORK)
        assertEquals(TaskType.WORK, viewModel.uiState.value.selectedType)
    }
}
