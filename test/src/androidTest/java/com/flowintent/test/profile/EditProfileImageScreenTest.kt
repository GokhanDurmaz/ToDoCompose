package com.flowintent.test.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.profile.ui.EditProfileImageScreen
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.profile.ui.vm.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class EditProfileImageScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(ProfileViewModel::class.java)
    private val profileUiState = MutableStateFlow(ProfileUiState())

    @Before
    fun setUp() {
        whenever(mockViewModel.uiState).thenReturn(profileUiState)
    }

    @Test
    fun editProfileImageScreen_displaysTitle() {
        composeTestRule.setContent {
            EditProfileImageScreen(viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Profile Picture").assertIsDisplayed()
    }
}
