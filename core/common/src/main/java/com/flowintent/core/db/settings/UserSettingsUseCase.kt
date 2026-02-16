package com.flowintent.core.db.settings

import com.flowintent.core.db.repository.SettingsRepository

class GetUserUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getUser()
}

class UpdateUsernameUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(newName: String) = repository.updateUsername(newName)
}

class LogoutUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.logout()
}
