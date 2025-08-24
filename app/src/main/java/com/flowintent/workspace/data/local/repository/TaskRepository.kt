package com.flowintent.workspace.data.local.repository

import com.flowintent.core.db.room.Task
import com.flowintent.core.db.room.dao.ToDoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val toDoDao: ToDoDao
) {
    fun  getAllTasks(): Flow<List<Task>> = toDoDao.getAllTasks()

    suspend fun insertTask(task: Task) {
        toDoDao.insertTask(task)
    }

    suspend fun findByTaskName(taskName: String) {
        toDoDao.findByTaskName(taskName)
    }

    suspend fun deleteTask(task: Task): Int {
        return toDoDao.delete(task)
    }
}
