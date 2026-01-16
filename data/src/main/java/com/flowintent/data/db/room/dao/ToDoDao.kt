package com.flowintent.data.db.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM Task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM  Task WHERE title LIKE :name LIMIT 1")
    suspend fun findByTaskName(name: String): Task

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun delete(task: Task): Int

    @Query("UPDATE Task SET title = :title, content = :content WHERE uid = :id")
    suspend fun updateTask(id: Int, title: String, content: TaskRes)
}
