package com.flowintent.core.di

import android.content.Context
import androidx.room.Room
import com.flowintent.core.db.room.ToDoDatabase
import com.flowintent.core.db.room.converters.TaskTypeConverters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
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
        taskTypeConverter: TaskTypeConverters
    ) = Room.databaseBuilder(
        app,
        ToDoDatabase::class.java,
        "todo_db"
    )
        .addTypeConverter(taskTypeConverter)
        .build()

    @Singleton
    @Provides
    fun provideToDoDao(toDoDatabase: ToDoDatabase) = toDoDatabase.toDoDao()

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .serializeNulls()
        .setStrictness(Strictness.STRICT)
        .create()

    @Singleton
    @Provides
    fun provideTaskTypeConverters(gson: Gson) = TaskTypeConverters(gson)
}
