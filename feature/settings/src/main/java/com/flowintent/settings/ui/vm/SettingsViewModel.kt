package com.flowintent.settings.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(theme = newTheme) }
    }

    fun onDndChange(enabled: Boolean) {
        _uiState.update { it.copy(doNotDisturb = enabled) }
    }

    fun onDndIntensityChange(intensity: Float) {
        _uiState.update { it.copy(dndIntensity = intensity) }
    }

    fun onLocaleChange(locale: String) {
        _uiState.update { it.copy(currentLocale = locale) }
    }

    fun onProfileClicked() {
        navigationDispatcher.navigateTo(ProfileNavigation.PROFILE_MAIN.route)
    }
}
