package com.flowintent.workspace.data.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flowintent.workspace.data.local.room.Task
import com.flowintent.workspace.data.local.room.converters.TaskTypeConverters
import com.flowintent.workspace.data.local.room.dao.ToDoDao

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(TaskTypeConverters::class)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}