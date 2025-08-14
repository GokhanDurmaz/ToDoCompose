package com.flowintent.workspace.data.local.repository

import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.data.local.room.dao.ToDoDao
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val toDoDao: ToDoDao
) {

    suspend fun insertTask(task: Task) {
        toDoDao.insertTask(task)
    }

    suspend fun  getAllTasks(): List<Task> {
        return toDoDao.getAllTasks()
    }

    suspend fun findByTaskName(taskName: String) {
        toDoDao.findByTaskName(taskName)
    }

    suspend fun deleteTask(task: Task) {
        toDoDao.delete(task)
    }
}
