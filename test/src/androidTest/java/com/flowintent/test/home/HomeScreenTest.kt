/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.home.ui.HomeScreen
import com.flowintent.home.ui.vm.HomeUiState
import com.flowintent.home.ui.vm.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockHomeViewModel = mock(HomeViewModel::class.java)
    private val homeUiState = MutableStateFlow(HomeUiState())

    @Before
    fun setUp() {
        whenever(mockHomeViewModel.uiState).thenReturn(homeUiState)
        // Set a dummy username flow if needed, but HomeScreen handles null username
        whenever(mockHomeViewModel.userName).thenReturn(MutableStateFlow("Test User"))
    }

    @Test
    fun homeScreen_displaysWelcomeMessage() {
        composeTestRule.setContent {
            HomeScreen(onCategoryClick = {}, homeViewModel = mockHomeViewModel)
        }

        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysCategoriesTitle() {
        composeTestRule.setContent {
            HomeScreen(onCategoryClick = {}, homeViewModel = mockHomeViewModel)
        }

        composeTestRule.onNodeWithText("Categories").assertIsDisplayed()
    }
}
