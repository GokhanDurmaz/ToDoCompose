package com.flowintent.core.db.settings

import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.repository.SettingsRepository

class GetThemeUseCase(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getTheme()
}

class UpdateThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(theme: AppTheme) = repository.updateTheme(theme)
}