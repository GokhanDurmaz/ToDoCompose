package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.core.db.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutSettingsViewModel @Inject constructor(
    private val repository: SettingsRepository
) : ViewModel() {

    val appVersion = repository.getAppVersion()

    fun openPrivacyPolicy() {
        repository.openPrivacyPolicy()
    }

    fun openTermsOfService() {
        repository.openTermsOfService()
    }
}
