/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue.trim(), statusMessage = null) }
    }

    fun resetPassword() {
        val email = _uiState.value.email
        viewModelScope.launch {
            forgetPasswordUseCase(email).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, statusMessage = null) }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false, statusMessage = "Reset link sent! Check your inbox." to false) }
                        delay(2000.milliseconds)
                        onNavigateBack()
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, statusMessage = resource.message to true) }
                }
            }
        }
    }

    fun setStatus(status: Pair<String, Boolean>?) {
        _uiState.update { it.copy(statusMessage = status) }
    }

    fun onNavigateBack() {
        navigationDispatcher.navigateBack()
    }
}
