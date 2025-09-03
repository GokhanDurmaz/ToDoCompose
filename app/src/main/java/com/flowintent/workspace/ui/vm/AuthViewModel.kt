package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.data.secure.EncryptedProtoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: EncryptedProtoRepository
) : ViewModel() {

    val token: StateFlow<String?> = repo.tokenFlow()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun saveToken(t: String) = viewModelScope.launch {
        repo.updateToken(t)
    }

    fun clearAll() = viewModelScope.launch {
        repo.clear()
    }
}
