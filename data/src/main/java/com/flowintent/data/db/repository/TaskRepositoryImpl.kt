package com.flowintent.data.db.repository

import com.flowintent.core.db.model.ActionType
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.data.db.room.dao.ToDoDao
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.util.Resource
import com.flowintent.core.util.parseDateToLong
import com.flowintent.network.network.TaskLlmEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

internal open class TaskRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val llmEngine: TaskLlmEngine,
    private val externalScope: CoroutineScope
): TaskRepository {
    override fun  getAllTasks(): Flow<List<Task>> = toDoDao.getAllTasks()

    override suspend fun insertTask(task: Task) {
        toDoDao.insertTask(task)
    }

    override suspend fun findByTaskName(taskName: String): Task {
        return toDoDao.findByTaskName(taskName)
    }

    override suspend fun deleteTask(task: Task): Int {
        return toDoDao.delete(task)
    }

    override suspend fun updateTask(id: Int, title: String, content: TaskRes) {
        toDoDao.updateTask(id, title, content)
    }

    override suspend fun insertSmartTask(userInput: String): Flow<Resource<Unit>> = callbackFlow {
        val currentLang = Locale.getDefault().language

        llmEngine.extractTask(userInput, currentLang) { title, timeText, category, action ->
            externalScope.launch {
                try {
                    val existingTasks = toDoDao.getAllTasks().first()

                    when (action) {
                        ActionType.DELETE -> {
                            val aiTitle = title.lowercase().trim()

                            val tasksToDelete = existingTasks.filter { task ->
                                val dbTitle = task.title.lowercase().trim()

                                val isDirectMatch = dbTitle.contains(aiTitle) || aiTitle.contains(dbTitle)

                                val aiWords = aiTitle.split(" ").filter { it.length > 2 }
                                val dbWords = dbTitle.split(" ").filter { it.length > 2 }

                                val commonWords = aiWords.intersect(dbWords.toSet())
                                val isWordMatch = commonWords.isNotEmpty()

                                isDirectMatch || isWordMatch
                            }

                            if (tasksToDelete.isNotEmpty()) {
                                tasksToDelete.forEach { toDoDao.delete(it) }
                            }
                        }

                        ActionType.UPDATE -> {
                            val taskToUpdate = existingTasks.find { task ->
                                val dbTitle = task.title.lowercase().trim()
                                val aiTitle = title.lowercase().trim()
                                dbTitle.contains(aiTitle) || aiTitle.contains(dbTitle)
                            }

                            taskToUpdate?.let {
                                toDoDao.updateTask(
                                    it.uid,
                                    title = it.title,
                                    content = TaskRes.TaskContent(content = title)
                                )
                            }
                        }

                        ActionType.ADD -> {
                            val finalDueDate = parseDateToLong(timeText, existingTasks)
                            val newTask = Task(
                                title = title,
                                content = TaskRes.TaskContent(content = title),
                                taskType = category,
                                dueDate = finalDueDate
                            )
                            toDoDao.insertTask(newTask)
                        }
                    }
                    trySend(Resource.Success(Unit))
                } catch (e: Exception) {
                    trySend(Resource.Error(e.localizedMessage ?: "İşlem Hatası"))
                } finally {
                }
            }
        }
        awaitClose { }
    }.onStart {
        emit(Resource.Loading)
    }
}
