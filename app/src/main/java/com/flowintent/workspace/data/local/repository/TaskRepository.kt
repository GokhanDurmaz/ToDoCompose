package com.flowintent.workspace.data.local.repository

import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.data.local.room.dao.ToDoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val toDoDao: ToDoDao
) {

    suspend fun insertTask(task: Task) {
        toDoDao.insertTask(task)
    }

    fun  getAllTasks(): Flow<List<Task>> = toDoDao.getAllTasks()

    suspend fun findByTaskName(taskName: String) {
        toDoDao.findByTaskName(taskName)
    }

    suspend fun deleteTask(task: Task): Int {
        return toDoDao.delete(task)
    }
}
