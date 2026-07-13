/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.SignUpScreen
import com.flowintent.auth.ui.vm.SignUpViewModel
import com.flowintent.auth.ui.vm.SignUpUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(SignUpViewModel::class.java)
    private val signUpUiState = MutableStateFlow(SignUpUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(signUpUiState.asStateFlow())
    }

    @Test
    fun signUpScreen_displaysCreateNewAccountText() {
        composeTestRule.setContent {
            SignUpScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Create New Account").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_displaysSignUpButton() {
        composeTestRule.setContent {
            SignUpScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Sign Up").assertIsDisplayed()
    }
}
