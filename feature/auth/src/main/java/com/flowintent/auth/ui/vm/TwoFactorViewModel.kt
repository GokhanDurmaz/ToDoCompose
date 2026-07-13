/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class TwoFactorViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TwoFactorUiState())
    val uiState = _uiState.asStateFlow()

    fun onCodeChange(newValue: String) {
        _uiState.update { it.copy(code = newValue, errorMessage = null) }
    }

    fun verifyTwoFactor() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500.milliseconds)
            _uiState.update { it.copy(isLoading = false) }
            onNavigateBack()
        }
    }

    fun resendTwoFactorCode() {
        viewModelScope.launch {
            delay(1000.milliseconds)
            _uiState.update { it.copy(errorMessage = "Code resent successfully!") }
        }
    }

    fun onNavigateBack() {
        navigationDispatcher.navigateBack()
    }
}
