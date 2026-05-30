package com.flowintent.test.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.profile.ui.ProfileScreen
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.profile.ui.vm.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(ProfileViewModel::class.java)
    private val profileUiState = MutableStateFlow(ProfileUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(profileUiState)
    }

    @Test
    fun profileScreen_displaysProfileTitle() {
        composeTestRule.setContent {
            ProfileScreen(profileViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun profileScreen_displaysAccountDetails() {
        composeTestRule.setContent {
            ProfileScreen(profileViewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Account Details").assertIsDisplayed()
    }
}
