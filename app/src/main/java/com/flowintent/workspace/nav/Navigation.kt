package com.flowintent.workspace.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RememberMe
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class Navigation(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME("home", "Home", Icons.Default.Home, "Home"),
    LIST_TODO("list_todo", "ToDo List", Icons.Default.FormatListNumbered, "ToDoList"),
    REMINDER("reminder", "Reminder", Icons.Default.RememberMe, "Reminder"),
    SETTINGS("settings", "Settings", Icons.Default.Settings, "Settings")
}