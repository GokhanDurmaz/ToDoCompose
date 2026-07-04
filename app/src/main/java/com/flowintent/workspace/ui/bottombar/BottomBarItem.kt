/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.FactCheck
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PendingActions
import androidx.compose.material.icons.rounded.Settings
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.R

val bottomNavItems = listOf(
    BottomNavItem(
        MainNavigation.HOME,
        R.string.home_label,
        Icons.Rounded.Home,
        R.string.home_label
    ),
    BottomNavItem(
        MainNavigation.LIST_TODO,
        R.string.todo_list_label,
        Icons.AutoMirrored.Rounded.FactCheck,
        R.string.todo_list_label
    ),
    BottomNavItem(
        MainNavigation.PENDING,
        R.string.pending_label,
        Icons.Rounded.PendingActions,
        R.string.pending_label
    ),
    BottomNavItem(
        MainNavigation.SETTINGS,
        R.string.settings_label,
        Icons.Rounded.Settings,
        R.string.settings_label
    )
)
