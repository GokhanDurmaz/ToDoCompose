/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.TwoFactorScreen
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.auth.ui.vm.TwoFactorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class TwoFactorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(AuthViewModel::class.java)
    private val twoFactorUiState = MutableStateFlow(TwoFactorUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.twoFactorUiState).thenReturn(twoFactorUiState)
    }

    @Test
    fun twoFactorScreen_displaysTitle() {
        composeTestRule.setContent {
            TwoFactorScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Two-Factor Authentication").assertIsDisplayed()
    }

    @Test
    fun twoFactorScreen_displaysSubtitle() {
        composeTestRule.setContent {
            TwoFactorScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Enter the 6-digit code sent to your device").assertIsDisplayed()
    }

    @Test
    fun twoFactorScreen_displaysVerifyButton() {
        composeTestRule.setContent {
            TwoFactorScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Verify and Proceed").assertIsDisplayed()
    }
}
