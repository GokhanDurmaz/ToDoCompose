package com.flowintent.core.db.repository

import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun  getTasks(query: String?, type: TaskType? = null): Flow<PagingData<Task>>

    fun findByTaskName(query: String): Flow<PagingData<Task>>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task): Int

    suspend fun deleteTaskById(id: Int): Int

    suspend fun updateTask(id: Int, title: String, content: TaskRes)

    suspend fun insertSmartTask(userInput: String): Flow<Resource<Unit>>
}
