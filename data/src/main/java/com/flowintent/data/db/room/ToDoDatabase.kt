package com.flowintent.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flowintent.core.db.SettingsPreferences
import com.flowintent.core.db.Task
import com.flowintent.data.db.room.converters.SettingsPreferencesConverter
import com.flowintent.data.db.room.converters.TaskTypeConverters
import com.flowintent.data.db.room.dao.SettingsDao
import com.flowintent.data.db.room.dao.ToDoDao

@Database(entities = [Task::class, SettingsPreferences::class], version = 1, exportSchema = false)
@TypeConverters(TaskTypeConverters::class, SettingsPreferencesConverter::class)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun toDoDao(): ToDoDao

    abstract fun settingsDao(): SettingsDao
}
