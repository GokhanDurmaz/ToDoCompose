package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.data.ToDoUiState
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
        val tasks: Map<TaskType, List<Task>> =
            LocalTaskDataProvider.allTasks.groupBy { it.tasktype }
        _uiState.value =
            ToDoUiState(
                tasks = tasks,
            )
    }

    fun updateCurrentTaskListScreen(taskType: TaskType) {
        _uiState.update {
            it.copy(
               taskListType = taskType
            )
        }
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                currentTask = it.tasks[it.taskListType]?.get(0) ?: LocalTaskDataProvider.defaultTask,
                isShowingMainScreen = true
            )
        }
    }

    fun updateDetailTaskScreenStates(task: Task) {
        _uiState.update {
            it.copy(
                currentTask = task,
                isShowingMainScreen = false
            )
        }
    }
}