/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.UpdateTokenUseCase
import com.flowintent.core.util.AnalyticsEvent
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.navigation.nav.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue.trim(), errorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun loginUser() {
        val state = _uiState.value
        viewModelScope.launch {
            signInUseCase(state.email, state.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> {
                        eventTracker.logEvent(AnalyticsEvent.LoginSuccess("email"))
                        _uiState.update { it.copy(isLoading = false) }
                        if (resource.data.isNotEmpty()) {
                            saveToken(resource.data)
                            onLoginSuccess()
                        }
                    }
                    is Resource.Error -> {
                        eventTracker.logEvent(AnalyticsEvent.LoginError("email", resource.message))
                        eventTracker.logMessage("Login failed: ${resource.message}")
                        _uiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    }
                }
            }
        }
    }

    private fun saveToken(t: String) = viewModelScope.launch {
        updateTokenUseCase(t)
    }

    private fun onLoginSuccess() {
        navigationDispatcher.navigateTo(MainNavigation.HOME.route) {
            popUpTo(AuthNavigation.SIGN_IN.route) { inclusive = true }
        }
    }

    fun onForgotPasswordClicked() {
        navigationDispatcher.navigateTo(AuthNavigation.FORGOT_PASSWORD.route)
    }

    fun onSignUpClicked() {
        navigationDispatcher.navigateTo(AuthNavigation.SIGN_UP.route)
    }

    fun setError(message: String?) {
        _uiState.update { it.copy(errorMessage = message) }
    }
}
