package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.AuthUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: EncryptedProtoRepository,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    val token: StateFlow<String?> = repo.tokenFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveToken(t: String) = viewModelScope.launch {
        repo.updateToken(t)
    }

    fun clearAll() = viewModelScope.launch {
        repo.clear()
    }

    fun registerUser(name: String, surname: String, email: String, password: String): Flow<Resource<Unit>> =
        authUseCase(name, surname, email, password)
}
