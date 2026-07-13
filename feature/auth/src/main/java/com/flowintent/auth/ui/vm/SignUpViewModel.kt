/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.SaveUserInfoUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.util.AnalyticsEvent
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onFirstNameChange(newValue: String) {
        _uiState.update { it.copy(firstName = newValue, errorMessage = null) }
    }

    fun onLastNameChange(newValue: String) {
        _uiState.update { it.copy(lastName = newValue, errorMessage = null) }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue.trim(), errorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun registerUser() {
        val state = _uiState.value
        viewModelScope.launch {
            signUpUseCase(state.firstName, state.lastName, state.email, state.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> {
                        eventTracker.logEvent(AnalyticsEvent.SignUpSuccess("email"))
                        _uiState.update { it.copy(isLoading = false) }
                        saveUser(state.firstName, state.lastName, state.email)
                        onNavigateBack()
                    }
                    is Resource.Error -> {
                        eventTracker.logEvent(AnalyticsEvent.SignUpError("email", resource.message))
                        eventTracker.logMessage("Sign up failed: ${resource.message}")
                        _uiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    }
                }
            }
        }
    }

    private fun saveUser(firstName: String, lastName: String, email: String) = viewModelScope.launch {
        saveUserInfoUseCase("$firstName $lastName", email)
    }

    fun onNavigateBack() {
        navigationDispatcher.navigateBack()
    }

    fun setError(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }
}
