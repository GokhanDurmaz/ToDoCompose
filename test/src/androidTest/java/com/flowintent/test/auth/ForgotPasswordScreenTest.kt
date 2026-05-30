package com.flowintent.test.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.ForgotPasswordScreen
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.auth.ui.vm.ForgotPasswordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ForgotPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(AuthViewModel::class.java)
    private val forgotPasswordUiState = MutableStateFlow(ForgotPasswordUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.forgotPasswordUiState).thenReturn(forgotPasswordUiState)
    }

    @Test
    fun forgotPasswordScreen_displaysHeader() {
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Forgot Password").assertIsDisplayed()
    }

    @Test
    fun forgotPasswordScreen_displaysSendButton() {
        composeTestRule.setContent {
            ForgotPasswordScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Send").assertIsDisplayed()
    }
}
