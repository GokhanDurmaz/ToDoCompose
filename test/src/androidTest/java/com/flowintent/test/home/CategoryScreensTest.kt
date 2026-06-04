/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.flowintent.home.ui.ArtScreen
import com.flowintent.home.ui.GymScreen
import com.flowintent.home.ui.HealthScreen
import org.junit.Rule
import org.junit.Test

class CategoryScreensTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun gymScreen_displaysTitle() {
        composeTestRule.setContent {
            GymScreen(onBack = {})
        }

        composeTestRule.onNodeWithText("Gym & Fitness").assertIsDisplayed()
    }

    @Test
    fun artScreen_displaysTitle() {
        composeTestRule.setContent {
            ArtScreen(onBack = {})
        }

        composeTestRule.onNodeWithText("Art Gallery").assertIsDisplayed()
    }

    @Test
    fun healthScreen_displaysTitle() {
        composeTestRule.setContent {
            HealthScreen(onBack = {})
        }

        composeTestRule.onNodeWithText("Health Tracker").assertIsDisplayed()
    }
}
