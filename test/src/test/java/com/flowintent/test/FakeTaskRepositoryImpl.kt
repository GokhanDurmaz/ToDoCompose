package com.flowintent.test

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeTaskRepositoryImpl : TaskRepository {
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    override fun getAllTasks(): Flow<List<Task>> = tasksFlow

    override suspend fun insertTask(task: Task) {
        tasksFlow.update { currentTasks -> currentTasks + task }
    }

    override suspend fun findByTaskName(taskName: String): Task {
        return tasksFlow.value.find { it.title == taskName }
            ?: throw NoSuchElementException("Task not found")
    }

    override suspend fun deleteTask(task: Task): Int {
        val initialSize = tasksFlow.value.size
        tasksFlow.update { currentTasks -> currentTasks.filterNot { it.uid == task.uid } }
        return if (tasksFlow.value.size < initialSize) 1 else 0
    }

    override suspend fun updateTask(id: Int, title: String, content: TaskRes) {
        tasksFlow.update { currentTasks ->
            currentTasks.map {
                if (it.uid == id) it.copy(title = title, content = content) else it
            }
        }
    }
}
