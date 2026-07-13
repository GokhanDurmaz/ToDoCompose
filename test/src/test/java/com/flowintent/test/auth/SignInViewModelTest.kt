/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import com.flowintent.auth.ui.vm.SignInViewModel
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.UpdateTokenUseCase
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
class SignInViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var signInUseCase: SignInUseCase
    @Mock private lateinit var updateTokenUseCase: UpdateTokenUseCase
    @Mock private lateinit var navigationDispatcher: NavigationDispatcher
    @Mock private lateinit var eventTracker: AppEventTracker

    private lateinit var viewModel: SignInViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        
        viewModel = SignInViewModel(
            signInUseCase,
            updateTokenUseCase,
            navigationDispatcher,
            eventTracker
        )
    }

    @Test
    fun `uiState updates when email changes`() {
        viewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `loginUser updates state to success`() = runTest {
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password")
        
        whenever(signInUseCase("test@example.com", "password"))
            .thenReturn(UseCaseScenarios.success("fake-token"))

        viewModel.loginUser()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `loginUser updates state to error when failure occurs`() = runTest {
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("wrong")

        whenever(signInUseCase("test@example.com", "wrong"))
            .thenReturn(UseCaseScenarios.error("Invalid credentials"))

        viewModel.loginUser()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("Invalid credentials", viewModel.uiState.value.errorMessage)
    }
}
