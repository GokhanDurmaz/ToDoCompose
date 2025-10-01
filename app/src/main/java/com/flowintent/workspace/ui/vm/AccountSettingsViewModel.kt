package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.source.ISettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val repository: ISettingsRepository
) : ViewModel() {

    val user = repository.getUser().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun updateUsername(newName: String) {
        viewModelScope.launch {
            repository.updateUsername(newName)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
