package com.flowintent.data.db.repository

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.flowintent.data.db.room.dao.ToDoDao
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.util.Resource
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

        llmEngine.extractTask(userInput, currentLang) { title, timeText, category ->
            externalScope.launch {
                try {
                    val finalDueDate = parseDateToLong(timeText)

                    val newTask = Task(
                        title = title,
                        content = TaskRes.TaskContent(content = userInput),
                        taskType = category,
                        cardColor = -123456,
                        iconColor = -1,
                        textColor = -1,
                        dueDate = finalDueDate
                    )

                    toDoDao.insertTask(newTask)

                    trySend(Resource.Success(Unit))
                } catch (e: Exception) {
                    trySend(Resource.Error(e.localizedMessage ?: "DB Error"))
                } finally {
                    close()
                }
            }
        }
        awaitClose { }
    }.onStart {
        emit(Resource.Loading)
    }

    private fun parseDateToLong(timeString: String?): Long {
        return try {
            if (timeString == "null" || timeString.isNullOrBlank()) {
                System.currentTimeMillis()
            } else {
                timeString.toLong()
            }
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
