package com.flowintent.core.db.repository

import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.User
import com.flowintent.core.db.settings.SettingsPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveUserPreferences(settingsPreferences: SettingsPreferences)

    suspend fun updateUserPreferences(uid: Int, appTheme: AppTheme)

    fun getUser(): Flow<User?>
    suspend fun updateUsername(name: String)
    suspend fun logout()

    fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)

    fun getTheme(): Flow<AppTheme>
    suspend fun updateTheme(theme: AppTheme)

    fun getAppVersion(): String
    fun openPrivacyPolicy()
    fun openTermsOfService()
}
