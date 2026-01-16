package com.flowintent.data.di

import com.flowintent.core.db.security.ISecurePrefsRepository
import com.flowintent.core.db.source.EncryptedProtoRepository
import com.flowintent.core.db.source.SettingsRepository
import com.flowintent.core.db.source.TaskCategoryRepository
import com.flowintent.core.db.source.TaskRepository
import com.flowintent.data.db.repository.SettingsRepositoryImpl
import com.flowintent.data.db.repository.TaskCategoryRepositoryImpl
import com.flowintent.data.db.repository.TaskRepositoryImpl
import com.flowintent.data.secure.EncryptedProtoRepositoryImpl
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
    internal abstract fun bindTaskRepository(taskRepository: TaskRepositoryImpl): TaskRepository

    @Singleton
    @Binds
    internal abstract fun bindTaskCategoryRepository(
        taskCategoryRepository: TaskCategoryRepositoryImpl
    ): TaskCategoryRepository

    @Singleton
    @Binds
    internal abstract fun bindSecurePrefsRepo(
        securePrefsRepositoryImpl: SecurePrefsRepositoryImpl
    ): ISecurePrefsRepository

    @Singleton
    @Binds
    internal abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Singleton
    @Binds
    internal abstract fun bindEncryptedProtoRepository(
        encryptedProtoRepositoryImpl: EncryptedProtoRepositoryImpl
    ): EncryptedProtoRepository
}
