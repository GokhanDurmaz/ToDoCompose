package com.flowintent.data.di

import com.flowintent.core.db.repository.AuthRepository
import com.flowintent.core.db.security.SecurePrefsRepository
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.db.repository.SettingsRepository
import com.flowintent.core.db.repository.TaskCategoryRepository
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.data.db.repository.AuthRepositoryImpl
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
internal abstract class RepositoryModule {

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
    ): SecurePrefsRepository

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

    @Singleton
    @Binds
    internal abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl) : AuthRepository
}
