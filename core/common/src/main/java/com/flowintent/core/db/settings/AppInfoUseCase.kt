/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.settings

import com.flowintent.core.db.repository.SettingsRepository

class GetAppVersionUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke() =
        repository.getAppVersion()
}

class OpenPrivacyPolicyUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke() =
        repository.openPrivacyPolicy()
}

class OpenTermsOfServiceUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke() =
        repository.openTermsOfService()
}
