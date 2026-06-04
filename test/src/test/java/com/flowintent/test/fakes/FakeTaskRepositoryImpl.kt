/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.fakes

import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.notification.TaskNotificationScheduler
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class FakeTaskRepositoryImpl(
    private val notificationScheduler: TaskNotificationScheduler? = null
) : TaskRepository {
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    override fun getTasks(query: String?, type: TaskType?): Flow<PagingData<Task>> = flow {
        tasksFlow.collect { list ->
            val filtered = list.filter { task ->
                val matchesSearch = if (query.isNullOrBlank()) true
                else task.title.contains(query, ignoreCase = true)

                val matchesType = if (type == null) true
                else task.taskType == type

                matchesSearch && matchesType
            }
            emit(PagingData.from(filtered))
        }
    }

    override fun findByTaskName(query: String): Flow<PagingData<Task>> = getTasks(query, null)

    override suspend fun insertTask(task: Task) {
        tasksFlow.update { it + task }
        notificationScheduler?.schedule(task)
    }

    override suspend fun deleteTask(task: Task): Int {
        val initialSize = tasksFlow.value.size
        tasksFlow.update { it.filterNot { item -> item.uid == task.uid } }
        val result = if (tasksFlow.value.size < initialSize) 1 else 0
        if (result == 1) notificationScheduler?.cancel(task)
        return result
    }

    override suspend fun deleteTaskById(id: Int): Int {
        val initialSize = tasksFlow.value.size
        tasksFlow.update { it.filterNot { task -> task.uid == id } }
        return if (tasksFlow.value.size < initialSize) 1 else 0
    }

    override suspend fun updateTask(id: Int, title: String, content: TaskRes) {
        tasksFlow.update { list ->
            list.map { if (it.uid == id) it.copy(title = title, content = content) else it }
        }
    }

    override suspend fun updateTask(task: Task) {
        tasksFlow.update { list ->
            list.map { if (it.uid == task.uid) task else it }
        }
        notificationScheduler?.schedule(task)
    }

    override suspend fun insertSmartTask(userInput: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        val newTask = Task(
            uid = (0..MAX_RANDOM_UID).random(),
            title = userInput,
            content = TaskRes.TaskContent(userInput),
            dueDate = 0L
        )
        insertTask(newTask)
        emit(Resource.Success(Unit))
    }

    override suspend fun getAllTasksRaw(): List<Task> = tasksFlow.value

    companion object {
        private const val MAX_RANDOM_UID = 1000
    }
}
