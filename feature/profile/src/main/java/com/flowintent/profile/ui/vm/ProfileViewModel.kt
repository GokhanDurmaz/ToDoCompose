package com.flowintent.profile.ui.vm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.auth.ObserveUserProfileUseCase
import com.flowintent.core.db.auth.UploadProfileUseCase
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val uploadProfileUseCase: UploadProfileUseCase,
    private val observeUploadProfileUseCase: ObserveUserProfileUseCase
): ViewModel() {

    private val _changePasswordState = MutableStateFlow<Resource<Unit>?>(null)
    val changePasswordState = _changePasswordState.asStateFlow()

    private val _uploadState = MutableStateFlow<Resource<String>?>(null)
    val uploadState = _uploadState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    init {
        observeUserProfile()
    }

    fun observeUserProfile() {
        viewModelScope.launch {
            observeUploadProfileUseCase().collect { result ->
                if (result is Resource.Success) {
                    _userProfile.value = result.data
                }
            }
        }
    }

    fun clearUploadState() {
        _uploadState.value = null
    }

    fun changePassword(currentPw: String, newPw: String) {
        viewModelScope.launch {
            changePasswordUseCase(currentPw, newPw).collect { result ->
                _changePasswordState.value = result
            }
        }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            uploadProfileUseCase(uri).collect { result ->
                _uploadState.value = result
            }
        }
    }

    fun clearChangePasswordState() {
        _changePasswordState.value = null
    }
    fun onBackClicked() {
        navigationDispatcher.navigateBack()
    }

    fun onNavigateTo(destination: ProfileNavigation) {
        navigationDispatcher.navigateTo(destination.route)
    }
}
