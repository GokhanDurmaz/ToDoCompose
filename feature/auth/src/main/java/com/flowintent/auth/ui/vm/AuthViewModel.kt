/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetEmailUseCase
import com.flowintent.core.db.auth.GetNameUseCase
import com.flowintent.core.db.auth.GetProfileImageUrlUseCase
import com.flowintent.core.db.auth.GetTokenUseCase
import com.flowintent.core.db.auth.GetUidUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.LogoutUseCase
import com.flowintent.core.db.auth.SaveUserInfoUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.auth.UpdateTokenUseCase
import com.flowintent.core.db.auth.UpdateUidUseCase
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.navigation.nav.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val updateTokenUseCase: UpdateTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getNameUseCase: GetNameUseCase,
    private val getEmailUseCase: GetEmailUseCase,
    private val getProfileImageUrlUseCase: GetProfileImageUrlUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val updateUidUseCase: UpdateUidUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _forgotPasswordUiState = MutableStateFlow(ForgotPasswordUiState())
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    private val _twoFactorUiState = MutableStateFlow(TwoFactorUiState())
    val twoFactorUiState = _twoFactorUiState.asStateFlow()

    val token: StateFlow<String?> = getTokenUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun onSignInEmailChange(newValue: String) {
        _signInUiState.update { it.copy(email = newValue.trim(), errorMessage = null) }
    }

    fun onSignInPasswordChange(newValue: String) {
        _signInUiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onSignUpFirstNameChange(newValue: String) {
        _signUpUiState.update { it.copy(firstName = newValue, errorMessage = null) }
    }

    fun onSignUpLastNameChange(newValue: String) {
        _signUpUiState.update { it.copy(lastName = newValue, errorMessage = null) }
    }

    fun onSignUpEmailChange(newValue: String) {
        _signUpUiState.update { it.copy(email = newValue.trim(), errorMessage = null) }
    }

    fun onSignUpPasswordChange(newValue: String) {
        _signUpUiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onForgotPasswordEmailChange(newValue: String) {
        _forgotPasswordUiState.update { it.copy(email = newValue.trim(), statusMessage = null) }
    }

    fun onTwoFactorCodeChange(newValue: String) {
        _twoFactorUiState.update { it.copy(code = newValue, errorMessage = null) }
    }

    val userName: StateFlow<String?> = getNameUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userEmail: StateFlow<String?> = getEmailUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val profileImageUrl: StateFlow<String?> = getProfileImageUrlUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isReady: StateFlow<Boolean> = getTokenUseCase()
        .map { true }
        .onStart {
            delay(1500.milliseconds)
            emit(true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun saveToken(t: String) = viewModelScope.launch {
        updateTokenUseCase(t)
    }

    fun registerUser() {
        val state = _signUpUiState.value
        viewModelScope.launch {
            signUpUseCase(state.firstName, state.lastName, state.email, state.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _signUpUiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> {
                        eventTracker.logEvent("sign_up_success")
                        _signUpUiState.update { it.copy(isLoading = false) }
                        saveUser(state.firstName, state.lastName, state.email)
                        onNavigateBack()
                    }
                    is Resource.Error -> {
                        eventTracker.logEvent("sign_up_error")
                        eventTracker.logMessage("Sign up failed: ${resource.message}")
                        _signUpUiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    }
                }
            }
        }
    }

    fun loginUser() {
        val state = _signInUiState.value
        viewModelScope.launch {
            signInUseCase(state.email, state.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _signInUiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> {
                        eventTracker.logEvent("login_success")
                        _signInUiState.update { it.copy(isLoading = false) }
                        if (resource.data.isNotEmpty()) {
                            saveToken(resource.data)
                            onLoginSuccess()
                        }
                    }
                    is Resource.Error -> {
                        eventTracker.logEvent("login_error")
                        eventTracker.logMessage("Login failed: ${resource.message}")
                        _signInUiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
                    }
                }
            }
        }
    }

    fun resetPassword() {
        val email = _forgotPasswordUiState.value.email
        viewModelScope.launch {
            forgetPasswordUseCase(email).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _forgotPasswordUiState.update { it.copy(isLoading = true, statusMessage = null) }
                    is Resource.Success -> {
                        _forgotPasswordUiState.update { it.copy(isLoading = false, statusMessage = "Reset link sent! Check your inbox." to false) }
                        delay(2000.milliseconds)
                        onNavigateBack()
                    }
                    is Resource.Error -> _forgotPasswordUiState.update { it.copy(isLoading = false, statusMessage = resource.message to true) }
                }
            }
        }
    }

    fun setSignInError(message: String?) {
        _signInUiState.update { it.copy(errorMessage = message) }
    }

    fun setSignUpError(message: String?) {
        _signUpUiState.update { it.copy(errorMessage = message) }
    }

    fun setForgotPasswordStatus(status: Pair<String, Boolean>?) {
        _forgotPasswordUiState.update { it.copy(statusMessage = status) }
    }

    fun saveUser(firstName: String, lastName: String, email: String) = viewModelScope.launch {
        saveUserInfoUseCase("$firstName $lastName", email)
    }

    fun fetchAndSaveUserProfileIfEmpty() = viewModelScope.launch {
        val currentName = getNameUseCase().firstOrNull()
        val currentEmail = getEmailUseCase().firstOrNull()
        val currentUid = getUidUseCase().firstOrNull()

        if (currentName.isNullOrBlank() || currentEmail.isNullOrBlank() || currentUid.isNullOrBlank()) {
            getUserProfileUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val profile = resource.data
                        saveUser(
                            firstName = profile.name,
                            lastName = profile.surname,
                            email = profile.email
                        )
                        updateUidUseCase(profile.uid)
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        }
    }

    fun onSignUpClicked() {
        navigationDispatcher.navigateTo(AuthNavigation.SIGN_UP.route)
    }

    fun onForgotPasswordClicked() {
        navigationDispatcher.navigateTo(AuthNavigation.FORGOT_PASSWORD.route)
    }

    fun onLoginSuccess() {
        navigationDispatcher.navigateTo(MainNavigation.HOME.route) {
            popUpTo(AuthNavigation.SIGN_IN.route) { inclusive = true }
        }
    }

    fun onNavigateBack() {
        navigationDispatcher.navigateBack()
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logoutUseCase().collect { resource ->
                when(resource) {
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        eventTracker.logEvent("logout_success")
                        navigationDispatcher.navigateTo(AuthNavigation.SIGN_IN.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    is Resource.Error -> {
                        eventTracker.logEvent("logout_error")
                        eventTracker.logMessage("Logout failed: ${resource.message}")
                    }
                }
            }
        }
    }

    fun verifyTwoFactor() {
        viewModelScope.launch {
            _twoFactorUiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(1500.milliseconds)
            _twoFactorUiState.update { it.copy(isLoading = false) }
            onNavigateBack()
        }
    }

    fun resendTwoFactorCode() {
        viewModelScope.launch {
            delay(1000.milliseconds)
            _twoFactorUiState.update { it.copy(errorMessage = "Code resent successfully!") }
        }
    }
}
