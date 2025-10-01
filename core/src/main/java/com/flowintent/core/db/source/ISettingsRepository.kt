package com.flowintent.core.db.source

import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.SettingsPreferences
import com.flowintent.core.db.User
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
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
