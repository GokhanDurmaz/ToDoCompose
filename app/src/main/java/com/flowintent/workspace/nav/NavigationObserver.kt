/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.flowintent.core.util.AnalyticsEvent
import com.flowintent.core.util.AppEventTracker

@Composable
fun NavigationObserver(
    navController: NavController,
    eventTracker: AppEventTracker
) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val route = destination.route ?: "unknown"
            eventTracker.logEvent(
                AnalyticsEvent.ScreenView(
                    screenName = route,
                    screenClass = destination.label?.toString()
                )
            )
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
