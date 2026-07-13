/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.settings

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.auth.ui.vm.SessionViewModel
import com.flowintent.settings.ui.AdvancedSettingsScreen
import com.flowintent.settings.ui.vm.SettingsUiState
import com.flowintent.settings.ui.vm.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AdvancedSettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockSettingsViewModel = mock(SettingsViewModel::class.java)
    private val mockSessionViewModel = mock(SessionViewModel::class.java)
    private val settingsUiState = MutableStateFlow(SettingsUiState())
    private val userName = MutableStateFlow<String?>("Test User")
    private val userEmail = MutableStateFlow<String?>("test@example.com")

    @Before
    fun setUp() {
        whenever(mockSettingsViewModel.uiState).thenReturn(settingsUiState.asStateFlow())
        whenever(mockSessionViewModel.userName).thenReturn(userName.asStateFlow())
        whenever(mockSessionViewModel.userEmail).thenReturn(userEmail.asStateFlow())
    }

    @Test
    fun advancedSettingsScreen_displaysThemeSection() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                sessionViewModel = mockSessionViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Theme").assertIsDisplayed()
    }

    @Test
    fun advancedSettingsScreen_displaysLanguageSection() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                sessionViewModel = mockSessionViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Language").assertIsDisplayed()
    }

    @Test
    fun advancedSettingsScreen_displaysLogoutButton() {
        composeTestRule.setContent {
            AdvancedSettingsScreen(
                sessionViewModel = mockSessionViewModel,
                settingsViewModel = mockSettingsViewModel
            )
        }

        composeTestRule.onNodeWithText("Logout").assertIsDisplayed()
    }
}
