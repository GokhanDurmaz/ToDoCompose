package com.flowintent.core.di

import com.flowintent.core.db.auth.AuthUseCase
import com.flowintent.core.db.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {
    @Singleton
    @Provides
    fun providesAuthUseCase(authRepository: AuthRepository) = AuthUseCase(authRepository)
}
