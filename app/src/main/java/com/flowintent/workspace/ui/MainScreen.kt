package com.flowintent.workspace.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import com.flowintent.uikit.util.MainContentType
import com.flowintent.uikit.util.MainNavigationType

@Composable
fun MainScreen(
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    val navigationType: MainNavigationType
    val contentType: MainContentType

    when(windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = MainNavigationType.BOTTOM_NAVIGATION
            contentType = MainContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = MainNavigationType.NAVIGATION_RAIL
            contentType = MainContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = MainNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = MainContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = MainNavigationType.BOTTOM_NAVIGATION
            contentType = MainContentType.LIST_ONLY
        }
    }

    ToDoHomeScreen()
}
