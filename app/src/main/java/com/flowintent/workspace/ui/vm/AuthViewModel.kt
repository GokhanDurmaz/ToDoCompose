package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ForgetPasswordUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.SignInUseCase
import com.flowintent.core.db.auth.SignUpUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: EncryptedProtoRepository,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {
    val token: StateFlow<String?> = repo.tokenFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    var emailInput = MutableStateFlow("")
        private set
    var passwordInput = MutableStateFlow("")
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val isSubmitEnabled: StateFlow<Boolean> = combine(
        emailInput,
        passwordInput,
        _isLoading
    ) { email, pass, loading ->
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        email.isNotBlank() && pass.isNotBlank() && isEmailValid && !loading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userName: StateFlow<String?> = repo.nameFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val userEmail: StateFlow<String?> = repo.emailFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isReady: StateFlow<Boolean> = repo.tokenFlow()
        .map {
            true
        }
        .onStart { delay(1500) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun saveToken(t: String) = viewModelScope.launch {
        repo.updateToken(t)
    }

    fun clearAll() = viewModelScope.launch {
        repo.clear()
    }

    fun registerUser(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        signUpUseCase(name, surname, email, password)

    fun onEmailChange(newValue: String) {
        emailInput.value = newValue.trim()
    }

    fun onPasswordChange(newValue: String) {
        passwordInput.value = newValue
    }

    fun loginUserWithState(): Flow<Resource<String>> {
        _isLoading.value = true
        return signInUseCase(emailInput.value, passwordInput.value).onEach {
            if (it !is Resource.Loading) _isLoading.value = false
        }
    }

    fun saveUser(firstName: String, lastName: String, email: String) = viewModelScope.launch {
        repo.saveUserInfo("$firstName $lastName", email)
    }

    fun fetchAndSaveUserProfileIfEmpty() = viewModelScope.launch {
        val currentName = repo.nameFlow().firstOrNull()
        val currentEmail = repo.emailFlow().firstOrNull()

        if (currentName.isNullOrBlank() || currentEmail.isNullOrBlank()) {
            getUserProfileUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val profile = resource.data
                        saveUser(
                            firstName = profile.name,
                            lastName = profile.surname,
                            email = profile.email
                        )
                    }
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                }
            }
        }
    }

    fun forgetPassword(email: String): Flow<Resource<Unit>>  = forgetPasswordUseCase(email)
}
