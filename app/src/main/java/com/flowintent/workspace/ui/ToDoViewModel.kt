package com.flowintent.workspace.ui

import androidx.lifecycle.ViewModel
import com.flowintent.workspace.data.ToDoUiState
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.data.local.LocalTaskDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ToDoViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ToDoUiState())
    val uiState: StateFlow<ToDoUiState> = _uiState

    init {
        initializeUIState()
    }

    private fun initializeUIState() {
        val songs: Map<TaskType, List<Task>> =
            LocalTaskDataProvider.allSongs.groupBy { it.song }
        _uiState.value =
            ToDoUiState(
                songs = songs,
            )
    }

    fun updateCurrentSongListScreen(songType: TaskType) {
        _uiState.update {
            it.copy(
               songListType = songType
            )
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentSong = it.songs[it.songListType]?.get(0) ?: LocalTaskDataProvider.defaultSong,
                isShowingMainScreen = true
            )
        }
    }

    fun updateDetailSongScreenStates(song: Task) {
        _uiState.update {
            it.copy(
                currentSong = song,
                isShowingMainScreen = false
            )
        }
    }
}
