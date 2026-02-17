package com.flowintent.core.db.settings

import com.flowintent.core.db.repository.SettingsRepository

class GetNotificationStatusUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getNotificationsEnabled()
}

class SetNotificationStatusUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(enabled: Boolean) = repository.setNotificationsEnabled(enabled)
}