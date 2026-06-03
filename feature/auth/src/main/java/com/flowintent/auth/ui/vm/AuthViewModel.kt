 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: EncryptedProtoRepository,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel()  {

    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _forgotPasswordUiState = MutableStateFlow(ForgotPasswordUiState())
    val forgotPasswordUiState = _forgotPasswordUiState.asStateFlow()

    val token: StateFlow<String?> = repo.tokenFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.Eagerly,
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

    val userName: StateFlow<String?> = repo.nameFlow()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    val userEmail: StateFlow<String?> = repo.emailFlow()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    val profileImageUrl: StateFlow<String?> = repo.profileImageUrlFlow()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    val isReady: StateFlow<Boolean> = repo.tokenFlow()
        .map { true }
        .onStart {
            delay(1500)
            emit(true)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = false
        )

    fun saveToken(t: String) = viewModelScope.launch {
        repo.updateToken(t)
    }

    fun clearAll() = viewModelScope.launch {
        repo.clear()
    }

    fun registerUser() {
        val state = _signUpUiState.value
        viewModelScope.launch {
            signUpUseCase(state.firstName, state.lastName, state.email, state.password).collect { resource ->
                when (resource) {
                    is Resource.Loading -> _signUpUiState.update { it.copy(isLoading = true, errorMessage = null) }
                    is Resource.Success -> {
                        _signUpUiState.update { it.copy(isLoading = false) }
                        saveUser(state.firstName, state.lastName, state.email)
                        onNavigateBack()
                    }
                    is Resource.Error -> _signUpUiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
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
                        _signInUiState.update { it.copy(isLoading = false) }
                        if (resource.data.isNotEmpty()) {
                            saveToken(resource.data)
                            onLoginSuccess()
                        }
                    }
                    is Resource.Error -> _signInUiState.update { it.copy(isLoading = false, errorMessage = resource.message) }
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
                        delay(2000)
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
        repo.saveUserInfo("$firstName $lastName", email)
    }

    fun fetchAndSaveUserProfileIfEmpty() = viewModelScope.launch {
        val currentName = repo.nameFlow().firstOrNull()
        val currentEmail = repo.emailFlow().firstOrNull()
        val currentUid = repo.uidFlow().firstOrNull()

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
                        repo.updateUid(profile.uid)
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
            repo.clear()
            navigationDispatcher.navigateTo(AuthNavigation.SIGN_IN.route) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }
}
