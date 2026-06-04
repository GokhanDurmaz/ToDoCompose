/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import com.flowintent.core.db.settings.GetNotificationStatusUseCase
import com.flowintent.core.db.settings.SetNotificationStatusUseCase
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.workspace.ui.vm.NotificationSettingsViewModel
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NotificationSettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getNotificationStatusUseCase: GetNotificationStatusUseCase
    @Mock
    private lateinit var setNotificationStatusUseCase: SetNotificationStatusUseCase

    private lateinit var viewModel: NotificationSettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(getNotificationStatusUseCase()).thenReturn(flowOf(true))
    }

    @Test
    fun `notificationsEnabled updates when use case emits`() = runTest {
        whenever(getNotificationStatusUseCase()).thenReturn(flowOf(false))
        viewModel = NotificationSettingsViewModel(getNotificationStatusUseCase, setNotificationStatusUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.notificationsEnabled.collect {}
        }

        assertEquals(false, viewModel.notificationsEnabled.value)
    }

    @Test
    fun `toggleNotifications calls setNotificationStatusUseCase`() = runTest {
        viewModel = NotificationSettingsViewModel(getNotificationStatusUseCase, setNotificationStatusUseCase)
        viewModel.toggleNotifications(true)
        verify(setNotificationStatusUseCase).invoke(true)
    }
}
