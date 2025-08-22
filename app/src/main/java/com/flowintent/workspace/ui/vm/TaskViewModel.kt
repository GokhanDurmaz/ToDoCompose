package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.workspace.data.local.repository.TaskRepository
import com.flowintent.workspace.data.local.room.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertTask(task: Task) {
        viewModelScope.async {
            repository.insertTask(task)
        }
    }

    fun findByTaskName(taskName: String) {
        viewModelScope.launch {
            repository.findByTaskName(taskName)
        }
    }

    fun deleteTask(task: Task): Boolean {
        viewModelScope.async {
            if (repository.deleteTask(task) == 1) {
                return@async true
            }
        }
        return false
    }
}
