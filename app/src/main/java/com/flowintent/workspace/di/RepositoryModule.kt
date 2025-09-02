package com.flowintent.workspace.di

import com.flowintent.core.db.source.ITaskCategoryRepository
import com.flowintent.core.db.source.ITaskRepository
import com.flowintent.data.db.repository.TaskCategoryRepositoryImpl
import com.flowintent.data.db.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(taskRepository: TaskRepositoryImpl): ITaskRepository

    @Singleton
    @Binds
    abstract fun bindTaskCategoryRepository(
        taskCategoryRepository: TaskCategoryRepositoryImpl
    ): ITaskCategoryRepository
}
