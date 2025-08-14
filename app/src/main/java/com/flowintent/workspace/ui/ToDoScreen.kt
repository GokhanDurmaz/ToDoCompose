package com.flowintent.workspace.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.ui.vm.TaskViewModel
import com.flowintent.workspace.ui.vm.ToDoViewModel
import com.flowintent.workspace.util.MainContentType
import com.flowintent.workspace.util.MainNavigationType

@Composable
fun ToDoScreen(
    viewModel: TaskViewModel,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
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

    ToDoHomeScreen(viewModel, modifier)
}
