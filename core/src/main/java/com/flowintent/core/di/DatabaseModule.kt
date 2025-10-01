package com.flowintent.core.di

import android.content.Context
import androidx.room.Room
import com.flowintent.core.db.room.ToDoDatabase
import com.flowintent.core.db.room.converters.SettingsPreferencesConverter
import com.flowintent.core.db.room.converters.TaskTypeConverters
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideToDoDatabase(
        @ApplicationContext app: Context,
        taskTypeConverter: TaskTypeConverters,
        settingsPreferencesConverter: SettingsPreferencesConverter
    ) = Room.databaseBuilder(
        app,
        ToDoDatabase::class.java,
        "todo_db"
    )
        .addTypeConverter(taskTypeConverter)
        .addTypeConverter(settingsPreferencesConverter)
        .build()

    @Singleton
    @Provides
    fun provideToDoDao(toDoDatabase: ToDoDatabase) = toDoDatabase.toDoDao()

    @Singleton
    @Provides
    fun provideSettingsDao(toDoDatabase: ToDoDatabase) = toDoDatabase.settingsDao()

    @Singleton
    @Provides
    fun provideTaskTypeConverters(gson: Gson) = TaskTypeConverters(gson)

    @Singleton
    @Provides
    fun provideSettingsConverters(gson: Gson) = SettingsPreferencesConverter(gson)
}
