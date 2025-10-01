package com.flowintent.data.di

import com.flowintent.core.db.security.ISecurePrefsRepository
import com.flowintent.core.db.source.ISettingsRepository
import com.flowintent.core.db.source.ITaskCategoryRepository
import com.flowintent.core.db.source.ITaskRepository
import com.flowintent.data.db.repository.SettingsRepositoryImpl
import com.flowintent.data.db.repository.TaskCategoryRepositoryImpl
import com.flowintent.data.db.repository.TaskRepositoryImpl
import com.flowintent.data.secure.SecurePrefsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(taskRepository: TaskRepositoryImpl): ITaskRepository

    @Singleton
    @Binds
    abstract fun bindTaskCategoryRepository(
        taskCategoryRepository: TaskCategoryRepositoryImpl
    ): ITaskCategoryRepository

    @Singleton
    @Binds
    abstract fun bindSecurePrefsRepo(
        securePrefsRepositoryImpl: SecurePrefsRepositoryImpl
    ): ISecurePrefsRepository

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): ISettingsRepository
}
