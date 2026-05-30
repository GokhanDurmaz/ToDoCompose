package com.flowintent.test.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.settings.ui.AdvancedSettingsScreen
import com.flowintent.settings.ui.vm.SettingsViewModel
import com.flowintent.settings.ui.vm.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AdvancedSettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockSettingsViewModel = mock(SettingsViewModel::class.java)
    private val mockAuthViewModel = mock(AuthViewModel::class.java)
    private val settingsUiState = MutableStateFlow(SettingsUiState())
    private val userName = MutableStateFlow<String?>("Test User")
    private val userEmail = MutableStateFlow<String?>("test@example.com")

    @Before
    fun setUp() {
        whenever(mockSettingsViewModel.uiState).thenReturn(settingsUiState)
        whenever(mockAuthViewModel.userName).thenReturn(userName)
        whenever(mockAuthViewModel.userEmail).thenReturn(userEmail)
    }

    @Test
    fun advancedSettingsScreen_displaysThemeSection() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                authViewModel = mockAuthViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Theme").assertIsDisplayed()
    }

    @Test
    fun advancedSettingsScreen_displaysLanguageSection() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                authViewModel = mockAuthViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Language").assertIsDisplayed()
    }

    @Test
    fun advancedSettingsScreen_displaysLogoutButton() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                authViewModel = mockAuthViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Logout").assertIsDisplayed()
    }
}
