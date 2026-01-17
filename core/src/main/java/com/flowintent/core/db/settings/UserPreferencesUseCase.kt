package com.flowintent.core.db.settings

import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.repository.SettingsRepository

class SaveUserPreferencesUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(settingsPreferences: SettingsPreferences) =
        repository.saveUserPreferences(settingsPreferences)
}

class UpdateUserPreferencesUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(uid: Int, appTheme: AppTheme) =
        repository.updateUserPreferences(uid, appTheme)
}