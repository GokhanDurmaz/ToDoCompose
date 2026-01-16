package com.flowintent.data.db.repository

import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.SettingsPreferences
import com.flowintent.core.db.User
import com.flowintent.core.db.room.dao.SettingsDao
import com.flowintent.core.db.source.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
): SettingsRepository {
    override suspend fun saveUserPreferences(settingsPreferences: SettingsPreferences) {
        settingsDao.saveUserPreferences(settingsPreferences)
    }

    override suspend fun updateUserPreferences(
        uid: Int,
        appTheme: AppTheme
    ) {
        settingsDao.updateUserPreferences(uid, appTheme)
    }

    override fun getUser(): Flow<User?> {
        return flowOf()
    }

    override suspend fun updateUsername(name: String) {
    }

    override suspend fun logout() {
    }

    override fun getNotificationsEnabled(): Flow<Boolean> {
        return flowOf()
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
    }

    override fun getTheme(): Flow<AppTheme> {
        return flowOf()
    }

    override suspend fun updateTheme(theme: AppTheme) {
    }

    override fun getAppVersion(): String {
        return ""
    }

    override fun openPrivacyPolicy() {
    }

    override fun openTermsOfService() {
    }
}
