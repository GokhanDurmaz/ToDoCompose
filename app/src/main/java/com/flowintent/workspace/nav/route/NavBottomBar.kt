 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.nav.route

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.ui.bottombar.bottomNavItems

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    navigationDispatcher: NavigationDispatcher,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = modifier
            .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
            .height(64.dp),
        shape = RoundedCornerShape(50.dp),
        color = if (isDark) {
            MaterialTheme.colorScheme.surfaceContainerHighest
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = if (isDark) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)) else null,
        tonalElevation = if (isDark) 12.dp else 4.dp,
        shadowElevation = 8.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentDestination?.hierarchy?.any { it.route == item.navigation.route } == true

                NavigationBarItem(
                    selected = isSelected,
                    alwaysShowLabel = false,
                    onClick = {
                        navigationDispatcher.navigateTo(item.navigation.route) {
                            popUpTo(MainNavigation.HOME.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = stringResource(item.contentDescriptionRes)
                        )
                    },
                    label = null
                )
            }
        }
    }
}
