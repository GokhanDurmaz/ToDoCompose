package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.settings.GetThemeUseCase
import com.flowintent.core.db.settings.UpdateThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val updateThemeUseCase: UpdateThemeUseCase
) : ViewModel() {

    val currentTheme = getThemeUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            AppTheme("default", "light")
        )

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        updateThemeUseCase(theme)
    }
}
