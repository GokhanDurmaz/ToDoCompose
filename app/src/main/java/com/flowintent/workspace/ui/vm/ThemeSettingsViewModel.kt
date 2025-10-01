package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.AppTheme
import com.flowintent.core.db.source.ISettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val repository: ISettingsRepository
) : ViewModel() {

    val currentTheme = repository.getTheme()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            AppTheme("default", "light")
        )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.updateTheme(theme)
        }
    }
}
