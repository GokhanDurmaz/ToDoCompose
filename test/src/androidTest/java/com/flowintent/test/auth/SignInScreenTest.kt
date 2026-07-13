/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.SignInScreen
import com.flowintent.auth.ui.vm.SignInViewModel
import com.flowintent.auth.ui.vm.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SignInScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(SignInViewModel::class.java)
    private val signInUiState = MutableStateFlow(SignInUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(signInUiState.asStateFlow())
    }

    @Test
    fun signInScreen_displaysWelcomeBackText() {
        composeTestRule.setContent {
            SignInScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Welcome Back").assertIsDisplayed()
    }

    @Test
    fun signInScreen_displaysSignInButton() {
        composeTestRule.setContent {
            SignInScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed()
    }
}
