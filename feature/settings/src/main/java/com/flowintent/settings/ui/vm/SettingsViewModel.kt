/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.settings.ui.vm

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.settings.GetLanguageUseCase
import com.flowintent.core.db.settings.GetProtoThemeUseCase
import com.flowintent.core.db.settings.UpdateLanguageUseCase
import com.flowintent.core.db.settings.UpdateProtoThemeUseCase
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val getProtoThemeUseCase: GetProtoThemeUseCase,
    private val updateProtoThemeUseCase: UpdateProtoThemeUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        syncInitialLocale()
        observeSettings()
    }

    private fun syncInitialLocale() {
        viewModelScope.launch {
            // Priority 1: Current active app locale
            val activeTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            if (activeTags.isNotEmpty()) {
                _uiState.update { it.copy(currentLocale = activeTags) }
            } else {
                // Priority 2: Persisted preference from DataStore
                val persistedLocale = getLanguageUseCase().first()
                if (persistedLocale != null) {
                    _uiState.update { it.copy(currentLocale = persistedLocale) }
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(persistedLocale))
                } else {
                    // Priority 3: Default to "en"
                    _uiState.update { it.copy(currentLocale = "en") }
                }
            }
        }
    }

    private fun observeSettings() {
        // Theme observation
        viewModelScope.launch {
            getProtoThemeUseCase().collectLatest { theme ->
                val currentTheme = theme ?: "Dark"
                _uiState.update { it.copy(theme = currentTheme) }
                val mode = when(currentTheme) {
                    "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                    "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                if (AppCompatDelegate.getDefaultNightMode() != mode) {
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
        
        // Language observation (only to keep state in sync with external changes if any)
        viewModelScope.launch {
            getLanguageUseCase().collectLatest { language ->
                if (language != null) {
                    // Only update if it matches what's actually applied to prevent stale reverts
                    val activeTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
                    if (activeTags == language) {
                        _uiState.update { it.copy(currentLocale = language) }
                    }
                }
            }
        }
    }

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(theme = newTheme) }
        viewModelScope.launch {
            updateProtoThemeUseCase(newTheme)
        }
        val mode = when(newTheme) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun onDndChange(enabled: Boolean) {
        _uiState.update { it.copy(doNotDisturb = enabled) }
    }

    fun onDndIntensityChange(intensity: Float) {
        _uiState.update { it.copy(dndIntensity = intensity) }
    }

    fun onLocaleChange(locale: String) {
        // 1. Apply to system immediately (this triggers activity recreation)
        val appLocale = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)

        // 2. Update local state
        _uiState.update { it.copy(currentLocale = locale) }

        // 3. Persist to DataStore
        viewModelScope.launch {
            updateLanguageUseCase(locale)
        }
    }

    fun onProfileClicked() {
        navigationDispatcher.navigateTo(ProfileNavigation.PROFILE_MAIN.route)
    }
}
