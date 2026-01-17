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

@Module
@InstallIn(SingletonComponent::class)
object SettingsUseCaseModule {
    @Provides
    fun providesSaveUserPreferencesUseCase(repository: SettingsRepository) =
        SaveUserPreferencesUseCase(repository)

    @Provides
    fun providesUpdateUserPreferencesUseCase(repository: SettingsRepository) =
        UpdateUserPreferencesUseCase(repository)

    @Provides
    fun providesGetUserUseCase(repository: SettingsRepository) =
        GetUserUseCase(repository)

    @Provides
    fun providesUpdateUsernameUseCase(repository: SettingsRepository) =
        UpdateUsernameUseCase(repository)

    @Provides
    fun providesLogoutUseCase(repository: SettingsRepository) =
        LogoutUseCase(repository)

    @Provides
    fun providesGetNotificationStatusUseCase(repository: SettingsRepository) =
        GetNotificationStatusUseCase(repository)

    @Provides
    fun providesSetNotificationStatusUseCase(repository: SettingsRepository) =
        SetNotificationStatusUseCase(repository)

    @Provides
    fun providesSetGetThemeUseCase(repository: SettingsRepository) =
        GetThemeUseCase(repository)

    @Provides
    fun providesSetUpdateThemeUseCase(repository: SettingsRepository) =
        UpdateThemeUseCase(repository)

    @Provides
    fun providesGetAppVersionUseCase(repository: SettingsRepository) =
        GetAppVersionUseCase(repository)

    @Provides
    fun providesOpenPrivacyPolicyUseCase(repository: SettingsRepository) =
        OpenPrivacyPolicyUseCase(repository)

    @Provides
    fun providesOpenTermsOfServiceUseCase(repository: SettingsRepository) =
        OpenTermsOfServiceUseCase(repository)
}
