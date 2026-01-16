package com.flowintent.data.db.repository

import androidx.annotation.VisibleForTesting
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.room.dao.ToDoDao
import com.flowintent.core.db.source.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
class TaskRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao
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
}
