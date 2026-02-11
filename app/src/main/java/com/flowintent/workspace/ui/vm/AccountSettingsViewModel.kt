package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.settings.GetUserUseCase
import com.flowintent.core.db.settings.LogoutUseCase
import com.flowintent.core.db.settings.UpdateUsernameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountSettingsViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val user = getUserUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    fun updateUsername(newName: String) = viewModelScope.launch {
        updateUsernameUseCase(newName)
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase()
    }
}
