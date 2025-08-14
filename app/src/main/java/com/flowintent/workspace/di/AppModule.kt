package com.flowintent.workspace.di

import android.content.Context
import androidx.room.Room
import com.flowintent.workspace.data.local.room.converters.TaskTypeConverters
import com.flowintent.workspace.data.local.room.db.ToDoDatabase
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
object AppModule {
    @Singleton
    @Provides
    fun provideToDoDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        ToDoDatabase::class.java,
        "todo_db"
    ).build()

    @Singleton
    @Provides
    fun provideToDoDao(toDoDatabase: ToDoDatabase) = toDoDatabase.toDoDao()

    @Singleton
    @Provides
    fun provideGson(): Gson? = GsonBuilder()
        .serializeNulls()
        .setStrictness(Strictness.STRICT)
        .create()
}
