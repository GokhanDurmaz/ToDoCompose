package com.flowintent.workspace.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RememberMe
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.flowintent.workspace.R

enum class MainNavigation(
    val route: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector,
    @StringRes val contentDescriptionRes: Int
) {
    HOME(
        route = "home",
        labelRes = R.string.home_label,
        icon = Icons.Default.Home,
        contentDescriptionRes = R.string.home_label
    ),
    LIST_TODO(
        route = "list_todo",
        labelRes = R.string.todo_list_label,
        icon = Icons.Default.FormatListNumbered,
        contentDescriptionRes = R.string.todo_list_label
    ),
    REMINDER(
        route = "reminder",
        labelRes = R.string.reminder_label,
        icon = Icons.Default.RememberMe,
        contentDescriptionRes = R.string.reminder_label
    ),
    SETTINGS(
        route = "settings",
        labelRes = R.string.settings_label,
        icon = Icons.Default.Settings,
        contentDescriptionRes = R.string.settings_label
    )
}
