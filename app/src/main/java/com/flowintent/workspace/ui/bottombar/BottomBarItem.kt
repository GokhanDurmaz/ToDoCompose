package com.flowintent.workspace.ui.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Settings
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.R

val bottomNavItems = listOf(
    BottomNavItem(
        MainNavigation.HOME,
        R.string.home_label,
        Icons.Default.Home,
        R.string.home_label
    ),
    BottomNavItem(
        MainNavigation.LIST_TODO,
        R.string.todo_list_label,
        Icons.Default.List,
        R.string.todo_list_label
    ),
    BottomNavItem(
        MainNavigation.PENDING,
        R.string.pending_label,
        Icons.Default.PendingActions,
        R.string.pending_label
    ),
    BottomNavItem(
        MainNavigation.SETTINGS,
        R.string.settings_label,
        Icons.Default.Settings,
        R.string.settings_label
    )
)
