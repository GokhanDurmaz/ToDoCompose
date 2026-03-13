package com.flowintent.core.di

import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.ObserveUserProfileUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.auth.UploadProfileUseCase
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
    fun providesSignUpUseCase(authRepository: AuthRepository) = SignUpUseCase(authRepository)

    @Singleton
    @Provides
    fun providesSignInUseCase(authRepository: AuthRepository) = SignInUseCase(authRepository)

    @Singleton
    @Provides
    fun providesGetUserProfileUseCase(authRepository: AuthRepository) =
        GetUserProfileUseCase(authRepository)

    @Singleton
    @Provides
    fun providesForgetPassword(authRepository: AuthRepository) = ForgetPasswordUseCase(authRepository)

    @Singleton
    @Provides
    fun providesChangePassword(authRepository: AuthRepository) = ChangePasswordUseCase(authRepository)

    @Singleton
    @Provides
    fun providesUploadProfile(authRepository: AuthRepository) = UploadProfileUseCase(authRepository)

    @Singleton
    @Provides
    fun providesObserveUserProfile(authRepository: AuthRepository) = ObserveUserProfileUseCase(authRepository)
}
