package com.flowintent.data.db.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType

@Dao
interface ToDoDao {
    @Query("SELECT * FROM tasks ORDER BY due_date DESC")
    fun getAllTasksPaging(): PagingSource<Int, Task>

    @Query("""
    SELECT * FROM tasks 
    WHERE (:type IS NULL OR tasks.task_type = :type)
    AND (
        :query IS NULL OR :query = '' OR 
        uid IN (SELECT rowid FROM tasks_fts WHERE tasks_fts MATCH :query)
    )
    ORDER BY tasks.due_date ASC
    """)
    fun getTasksPaging(query: String?, type: TaskType?): PagingSource<Int, Task>

    @Query("""
        SELECT tasks.* FROM tasks 
        JOIN tasks_fts ON tasks.uid = tasks_fts.rowid 
        WHERE tasks_fts MATCH :query
    """)
    fun searchTasksPaging(query: String): PagingSource<Int, Task>

    @Query("""
        SELECT tasks.* FROM tasks 
        JOIN tasks_fts ON tasks.uid = tasks_fts.rowid 
        WHERE tasks_fts MATCH :query LIMIT 1
    """)
    suspend fun findFirstTaskByMatch(query: String): Task?

    @Insert
    suspend fun insertTask(task: Task): Long

    @Delete
    suspend fun delete(task: Task): Int

    @Query("DELETE FROM tasks WHERE uid = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM tasks_fts WHERE rowid = :id")
    suspend fun deleteFtsById(id: Int): Int

    @Query("UPDATE tasks SET title = :title, content = :content WHERE uid = :id")
    suspend fun updateTask(id: Int, title: String, content: TaskRes)
}
