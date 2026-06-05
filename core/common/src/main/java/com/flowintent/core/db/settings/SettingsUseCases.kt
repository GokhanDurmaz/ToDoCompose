/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.settings

import com.flowintent.core.db.model.AppTheme
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.db.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppVersionUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.getAppVersion()
}

class OpenPrivacyPolicyUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.openPrivacyPolicy()
}

class OpenTermsOfServiceUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.openTermsOfService()
}

class GetUserUseCase @Inject constructor(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getUser()
}

class UpdateUsernameUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(newName: String) = repository.updateUsername(newName)
}

class LogoutUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.logout()
}

class GetLanguageUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.languageFlow()
}

class UpdateLanguageUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(language: String) = repository.updateLanguage(language)
}

class GetProtoThemeUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.themeFlow()
}

class UpdateProtoThemeUseCase @Inject constructor(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(theme: String) = repository.updateTheme(theme)
}

class GetThemeUseCase @Inject constructor(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getTheme()
}

class UpdateThemeUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(theme: AppTheme) = repository.updateTheme(theme)
}

class SaveUserPreferencesUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(settingsPreferences: SettingsPreferences) =
        repository.saveUserPreferences(settingsPreferences)
}

class UpdateUserPreferencesUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(uid: Int, appTheme: AppTheme) =
        repository.updateUserPreferences(uid, appTheme)
}

class GetNotificationStatusUseCase @Inject constructor(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getNotificationsEnabled()
}

class SetNotificationStatusUseCase @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) = repository.setNotificationsEnabled(enabled)
}
