package com.flowintent.core.di

import com.flowintent.core.db.repository.SettingsRepository
import com.flowintent.core.db.settings.GetAppVersionUseCase
import com.flowintent.core.db.settings.GetNotificationStatusUseCase
import com.flowintent.core.db.settings.GetThemeUseCase
import com.flowintent.core.db.settings.GetUserUseCase
import com.flowintent.core.db.settings.LogoutUseCase
import com.flowintent.core.db.settings.OpenPrivacyPolicyUseCase
import com.flowintent.core.db.settings.OpenTermsOfServiceUseCase
import com.flowintent.core.db.settings.SaveUserPreferencesUseCase
import com.flowintent.core.db.settings.SetNotificationStatusUseCase
import com.flowintent.core.db.settings.UpdateThemeUseCase
import com.flowintent.core.db.settings.UpdateUserPreferencesUseCase
import com.flowintent.core.db.settings.UpdateUsernameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsUseCaseModule {
    @Singleton
    @Provides
    fun providesSaveUserPreferencesUseCase(repository: SettingsRepository) =
        SaveUserPreferencesUseCase(repository)

    @Singleton
    @Provides
    fun providesUpdateUserPreferencesUseCase(repository: SettingsRepository) =
        UpdateUserPreferencesUseCase(repository)

    @Singleton
    @Provides
    fun providesGetUserUseCase(repository: SettingsRepository) =
        GetUserUseCase(repository)

    @Singleton
    @Provides
    fun providesUpdateUsernameUseCase(repository: SettingsRepository) =
        UpdateUsernameUseCase(repository)

    @Singleton
    @Provides
    fun providesLogoutUseCase(repository: SettingsRepository) =
        LogoutUseCase(repository)

    @Singleton
    @Provides
    fun providesGetNotificationStatusUseCase(repository: SettingsRepository) =
        GetNotificationStatusUseCase(repository)

    @Singleton
    @Provides
    fun providesSetNotificationStatusUseCase(repository: SettingsRepository) =
        SetNotificationStatusUseCase(repository)

    @Singleton
    @Provides
    fun providesSetGetThemeUseCase(repository: SettingsRepository) =
        GetThemeUseCase(repository)

    @Singleton
    @Provides
    fun providesSetUpdateThemeUseCase(repository: SettingsRepository) =
        UpdateThemeUseCase(repository)

    @Singleton
    @Provides
    fun providesGetAppVersionUseCase(repository: SettingsRepository) =
        GetAppVersionUseCase(repository)

    @Singleton
    @Provides
    fun providesOpenPrivacyPolicyUseCase(repository: SettingsRepository) =
        OpenPrivacyPolicyUseCase(repository)

    @Singleton
    @Provides
    fun providesOpenTermsOfServiceUseCase(repository: SettingsRepository) =
        OpenTermsOfServiceUseCase(repository)
}
