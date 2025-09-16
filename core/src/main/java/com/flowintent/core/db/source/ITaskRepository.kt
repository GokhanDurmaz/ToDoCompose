package com.flowintent.core.db.source

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun  getAllTasks(): Flow<List<Task>>

    suspend fun insertTask(task: Task)

    suspend fun findByTaskName(taskName: String): Task

    suspend fun deleteTask(task: Task): Int

    suspend fun updateTask(id: Int, title: String, content: TaskRes)
}
