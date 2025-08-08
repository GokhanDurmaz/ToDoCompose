package com.flowintent.workspace.data

import com.flowintent.workspace.data.local.LocalTaskDataProvider

data class ToDoUiState(
    val songs: Map<TaskType, List<Task>> = emptyMap(),
    val songListType: TaskType = TaskType.LOCAL_SONGS,
    val currentSong: Task = LocalTaskDataProvider.defaultSong,
    val isShowingMainScreen: Boolean = true
) {
    val currentSongs: List<Task>?? by lazy { songs[songListType] }
}
