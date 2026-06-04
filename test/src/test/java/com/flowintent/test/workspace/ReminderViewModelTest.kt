/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.workspace.ui.vm.ReminderViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class ReminderViewModelTest {

    @Mock
    private lateinit var navigationDispatcher: NavigationDispatcher

    private lateinit var viewModel: ReminderViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ReminderViewModel(navigationDispatcher)
    }

    @Test
    fun `onBackClicked navigates back`() {
        viewModel.onBackClicked()
        verify(navigationDispatcher).navigateBack()
    }

    @Test
    fun `onReminderClicked navigates to task detail`() {
        viewModel.onReminderClicked(123L)
        verify(navigationDispatcher).navigateTo("task_detail/123")
    }
}
