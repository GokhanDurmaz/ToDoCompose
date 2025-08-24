package com.flowintent.workspace.data

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskType
import com.flowintent.workspace.data.local.LocalTaskDataProvider

data class ToDoUiState(
    val tasks: Map<TaskType, List<Task>> = emptyMap(),
    val taskListType: TaskType = TaskType.LOCAL_TASKS,
    val currentTask: Task = LocalTaskDataProvider.defaultTask,
    val isShowingMainScreen: Boolean = true
) {
    val currentTasks: List<Task>?? by lazy { tasks[taskListType] }
}
