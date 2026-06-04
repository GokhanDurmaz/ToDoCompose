/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.settings.ui.vm

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val repo: EncryptedProtoRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            repo.languageFlow().collectLatest { language ->
                val current = language ?: AppCompatDelegate.getApplicationLocales().toLanguageTags().ifEmpty { "en" }
                _uiState.update { it.copy(currentLocale = current) }
            }
        }
        viewModelScope.launch {
            repo.themeFlow().collectLatest { theme ->
                _uiState.update { it.copy(theme = theme ?: "Dark") }
            }
        }
    }

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(theme = newTheme) }
        viewModelScope.launch {
            repo.updateTheme(newTheme)
        }
    }

    fun onDndChange(enabled: Boolean) {
        _uiState.update { it.copy(doNotDisturb = enabled) }
    }

    fun onDndIntensityChange(intensity: Float) {
        _uiState.update { it.copy(dndIntensity = intensity) }
    }

    fun onLocaleChange(locale: String) {
        _uiState.update { it.copy(currentLocale = locale) }
        viewModelScope.launch {
            repo.updateLanguage(locale)
        }
    }

    fun onProfileClicked() {
        navigationDispatcher.navigateTo(ProfileNavigation.PROFILE_MAIN.route)
    }
}
