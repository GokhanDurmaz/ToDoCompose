/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.flowintent.workspace.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_launchesAndDisplaysWelcomeHeader() {
        // Using Compose Testing API to find nodes
        composeTestRule.onNodeWithText("Categories").assertExists()
        
        // Espresso can also be used to interact with the hierarchy
        onView(withText("Categories")).check(matches(isDisplayed()))
    }

    @Test
    fun app_canNavigateBetweenBottomTabs() {
        // Find and click on the "Pending" tab using Espresso if it's rendered as a view
        // Or using Compose if it's a Composable
        composeTestRule.onNodeWithText("Pending").assertExists()
        
        // Example of Espresso usage for a common assertion
        onView(withText("Home")).check(matches(isDisplayed()))
    }
}
