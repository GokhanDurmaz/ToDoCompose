package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.settings.GetNotificationStatusUseCase
import com.flowintent.core.db.settings.SetNotificationStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val getNotificationStatusUseCase: GetNotificationStatusUseCase,
    private val setNotificationStatusUseCase: SetNotificationStatusUseCase
) : ViewModel() {

    val notificationsEnabled = getNotificationStatusUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun toggleNotifications(enabled: Boolean) = viewModelScope.launch {
        setNotificationStatusUseCase(enabled)
    }
}
