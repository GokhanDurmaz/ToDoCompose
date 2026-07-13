/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import com.flowintent.auth.ui.vm.SignUpViewModel
import com.flowintent.core.db.auth.SaveUserInfoUseCase
import com.flowintent.core.db.auth.SignUpUseCase
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
class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var signUpUseCase: SignUpUseCase
    @Mock private lateinit var saveUserInfoUseCase: SaveUserInfoUseCase
    @Mock private lateinit var navigationDispatcher: NavigationDispatcher
    @Mock private lateinit var eventTracker: AppEventTracker

    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        
        viewModel = SignUpViewModel(
            signUpUseCase,
            saveUserInfoUseCase,
            navigationDispatcher,
            eventTracker
        )
    }

    @Test
    fun `registerUser updates state to success`() = runTest {
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onEmailChange("john@example.com")
        viewModel.onPasswordChange("password")

        whenever(signUpUseCase("John", "Doe", "john@example.com", "password"))
            .thenReturn(UseCaseScenarios.success(Unit))

        viewModel.registerUser()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }
}
