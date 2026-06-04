/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.settings

import com.flowintent.core.db.repository.EncryptedProtoRepository
import kotlinx.coroutines.flow.Flow

class GetLanguageUseCase(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.languageFlow()
}

class UpdateLanguageUseCase(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(language: String) = repository.updateLanguage(language)
}

class GetProtoThemeUseCase(private val repository: EncryptedProtoRepository) {
    operator fun invoke(): Flow<String?> = repository.themeFlow()
}

class UpdateProtoThemeUseCase(private val repository: EncryptedProtoRepository) {
    suspend operator fun invoke(theme: String) = repository.updateTheme(theme)
}
