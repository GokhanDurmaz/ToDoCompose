package com.flowintent.workspace.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.util.MainContentType
import com.flowintent.workspace.util.MainNavigationType

@Composable
fun MainScreen(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navigationType: MainNavigationType
    val contentType: MainContentType
    val viewModel: ToDoViewModel = viewModel()
    val songUiState = viewModel.uiState.collectAsState().value

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

    MainHomeScreen(
        navigationType = navigationType,
        contentType = contentType,
        songUiState = songUiState,
        onTabPressed = { songType: TaskType ->
            viewModel.updateCurrentSongListScreen(
                songType = songType
            )
            viewModel.resetHomeScreenStates()
        },
        onSongCardPressed = { song: Task ->
            viewModel.updateDetailSongScreenStates(
                song = song
            )
        },
        onDetailScreenBackPressed = {
            viewModel.resetHomeScreenStates()
        },
        modifier = modifier
    )
}
