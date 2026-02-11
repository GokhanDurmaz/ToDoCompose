package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.core.db.settings.GetAppVersionUseCase
import com.flowintent.core.db.settings.OpenPrivacyPolicyUseCase
import com.flowintent.core.db.settings.OpenTermsOfServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val openPrivacyPolicyUseCase: OpenPrivacyPolicyUseCase,
    private val openTermsOfServiceUseCase: OpenTermsOfServiceUseCase

) : ViewModel() {
    suspend fun getAppVersion() = getAppVersionUseCase()

    suspend fun openPrivacyPolicy() = openPrivacyPolicyUseCase()

    suspend fun openTermsOfService() = openTermsOfServiceUseCase()
}
