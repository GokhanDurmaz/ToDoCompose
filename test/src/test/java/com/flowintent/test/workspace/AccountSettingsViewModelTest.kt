/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import com.flowintent.core.db.model.User
import com.flowintent.core.db.settings.GetUserUseCase
import com.flowintent.core.db.settings.LogoutUseCase
import com.flowintent.core.db.settings.UpdateUsernameUseCase
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.workspace.ui.vm.AccountSettingsViewModel
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
class AccountSettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getUserUseCase: GetUserUseCase
    @Mock
    private lateinit var updateUsernameUseCase: UpdateUsernameUseCase
    @Mock
    private lateinit var logoutUseCase: LogoutUseCase

    private lateinit var viewModel: AccountSettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(getUserUseCase()).thenReturn(flowOf(null))
    }

    @Test
    fun `user updates when use case emits`() = runTest {
        val user = User(uid = 1, name = "John", mail = "john@example.com", profile = "")
        whenever(getUserUseCase()).thenReturn(flowOf(user))
        viewModel = AccountSettingsViewModel(getUserUseCase, updateUsernameUseCase, logoutUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.user.collect {}
        }

        assertEquals(user, viewModel.user.value)
    }

    @Test
    fun `updateUsername calls use case`() = runTest {
        viewModel = AccountSettingsViewModel(getUserUseCase, updateUsernameUseCase, logoutUseCase)
        viewModel.updateUsername("NewName")
        verify(updateUsernameUseCase).invoke("NewName")
    }

    @Test
    fun `logout calls use case`() = runTest {
        viewModel = AccountSettingsViewModel(getUserUseCase, updateUsernameUseCase, logoutUseCase)
        viewModel.logout()
        verify(logoutUseCase).invoke()
    }
}
