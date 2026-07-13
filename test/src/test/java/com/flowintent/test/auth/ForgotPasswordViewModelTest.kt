/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import com.flowintent.auth.ui.vm.ForgotPasswordViewModel
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.util.AppEventTracker
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.test.scenarios.UseCaseScenarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ForgotPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var forgetPasswordUseCase: ForgetPasswordUseCase
    @Mock private lateinit var navigationDispatcher: NavigationDispatcher
    @Mock private lateinit var eventTracker: AppEventTracker

    private lateinit var viewModel: ForgotPasswordViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        
        viewModel = ForgotPasswordViewModel(
            forgetPasswordUseCase,
            navigationDispatcher,
            eventTracker
        )
    }

    @Test
    fun `resetPassword updates state to success`() = runTest {
        viewModel.onEmailChange("test@example.com")

        whenever(forgetPasswordUseCase("test@example.com"))
            .thenReturn(UseCaseScenarios.success(Unit))

        viewModel.resetPassword()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Reset link sent! Check your inbox." to false, viewModel.uiState.value.statusMessage)
    }
}
