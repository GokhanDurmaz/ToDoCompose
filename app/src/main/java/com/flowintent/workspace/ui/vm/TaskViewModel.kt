package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.workspace.data.local.repository.TaskRepository
import com.flowintent.workspace.data.local.room.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    fun getAllTasks(): List<Task> = runBlocking {
        repository.getAllTasks()
    }

    fun insertTask(task: Task) {
       runBlocking {
           repository.insertTask(task)
       }
    }

    fun findByTaskName(taskName: String) {
        viewModelScope.launch {
            repository.findByTaskName(taskName)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
