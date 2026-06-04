 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.db.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flowintent.core.db.model.ActionType
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.notification.TaskNotificationScheduler
import com.flowintent.core.util.Resource
import com.flowintent.core.util.parseDateToLong
import com.flowintent.data.db.room.dao.ToDoDao
import com.flowintent.network.network.TaskLlmEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

 internal open class TaskRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val llmEngine: TaskLlmEngine,
    private val externalScope: CoroutineScope,
    private val notificationScheduler: TaskNotificationScheduler
): TaskRepository {
    override fun getTasks(query: String?, type: TaskType?): Flow<PagingData<Task>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { toDoDao.getTasksPaging(query, type) }
        ).flow
    }

    override suspend fun getAllTasksRaw(): List<Task> {
        return toDoDao.getAllTasks()
    }

    override suspend fun insertTask(task: Task) {
        val id = toDoDao.insertTask(task)
        notificationScheduler.schedule(task.copy(uid = id.toInt()))
    }

    override fun findByTaskName(query: String): Flow<PagingData<Task>> {
        return Pager(PagingConfig(10)) { toDoDao.searchTasksPaging("$query*") }.flow
    }

    override suspend fun deleteTask(task: Task): Int {
        notificationScheduler.cancel(task)
        return toDoDao.delete(task)
    }

    override suspend fun deleteTaskById(id: Int): Int {
        toDoDao.deleteFtsById(id)
        return toDoDao.deleteById(id)
    }

    override suspend fun updateTask(id: Int, title: String, content: TaskRes) {
        toDoDao.updateTask(id, title, content)
    }

    override suspend fun updateTask(task: Task) {
        toDoDao.update(task)
        notificationScheduler.schedule(task)
    }

    override suspend fun insertSmartTask(userInput: String): Flow<Resource<Unit>> = callbackFlow {
        val currentLang = Locale.getDefault().language

        llmEngine.extractTask(userInput, currentLang) { title, timeText, category, action ->
            externalScope.launch {
                try {
                    val ftsQuery = title.trim().split(" ").joinToString(" OR ") { "$it*" }

                    when (action) {
                        ActionType.DELETE -> {
                            val taskToDelete = toDoDao.findFirstTaskByMatch(ftsQuery)
                            taskToDelete?.let {
                                notificationScheduler.cancel(it)
                                toDoDao.delete(it)
                            }
                        }

                        ActionType.UPDATE -> {
                            val taskToUpdate = toDoDao.findFirstTaskByMatch(ftsQuery)
                            taskToUpdate?.let {
                                val updatedContent = TaskRes.TaskContent(title)
                                toDoDao.updateTask(it.uid, it.title, updatedContent)
                                notificationScheduler.schedule(it.copy(content = updatedContent))
                            }
                        }

                        ActionType.ADD -> {
                            val newTask = Task(
                                title = title.ifBlank { userInput },
                                content = TaskRes.TaskContent(title),
                                taskType = category,
                                dueDate = parseDateToLong(timeText, emptyList())
                            )
                            val id = toDoDao.insertTask(newTask)
                            notificationScheduler.schedule(newTask.copy(uid = id.toInt()))
                        }
                    }
                    trySend(Resource.Success(Unit))
                } catch (e: Exception) {
                    trySend(Resource.Error(e.localizedMessage ?: "Error"))
                }
            }
        }
        awaitClose { }
    }.onStart { emit(Resource.Loading) }
}
