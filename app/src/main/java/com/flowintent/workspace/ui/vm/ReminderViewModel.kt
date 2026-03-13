package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.navigation.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
): ViewModel() {

    fun onBackClicked() {
        navigationDispatcher.navigateBack()
    }

    fun onReminderClicked(taskId: Long) {
        navigationDispatcher.navigateTo("task_detail/$taskId")
    }
}
