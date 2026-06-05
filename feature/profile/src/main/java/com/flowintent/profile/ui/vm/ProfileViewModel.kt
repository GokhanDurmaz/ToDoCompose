 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui.vm

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.auth.GetUidUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.SaveProfileImageUrlUseCase
import com.flowintent.core.db.profile.DownloadAndSaveUseCase
import com.flowintent.core.db.profile.GetLocalAvatarUseCase
import com.flowintent.core.db.profile.ObserveUserProfileUseCase
import com.flowintent.core.db.profile.UploadProfileUseCase
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navigationDispatcher: NavigationDispatcher,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val uploadProfileUseCase: UploadProfileUseCase,
    private val downloadAndSaveUseCase: DownloadAndSaveUseCase,
    private val getLocalAvatarUseCase: GetLocalAvatarUseCase,
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val saveProfileImageUrlUseCase: SaveProfileImageUrlUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        observeUid()
        fetchUserProfile()
        observeUserProfile()
        observeBitmapLoading()
    }

    private fun observeUid() {
        viewModelScope.launch {
            getUidUseCase().collect { uid ->
                _uiState.update { it.copy(userUid = uid) }
            }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            getUserProfileUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(userProfile = result.data, isProfileLoading = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isProfileLoading = false) }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isProfileLoading = true) }
                    }
                }
            }
        }
    }

    private fun observeUserProfile() {
        viewModelScope.launch {
            observeUserProfileUseCase().collect { result ->
                if (result is Resource.Success) {
                    _uiState.update { it.copy(userProfile = result.data, isProfileLoading = false) }
                }
            }
        }
    }

    private fun observeBitmapLoading() {
        viewModelScope.launch {
            _uiState.map { it.userUid }.collectLatest { uid ->
                if (uid != null) {
                    // Always try to load local bitmap whenever UID is available/changed
                    val localBitmap = getLocalAvatarUseCase(uid)
                    if (localBitmap != null) {
                        _uiState.update { it.copy(profileBitmap = localBitmap) }
                    } else {
                        // Only download if local is not found
                        val bitmap = downloadAndSaveUseCase(uid)
                        if (bitmap != null) {
                            _uiState.update { it.copy(profileBitmap = bitmap) }
                        }
                    }
                }
            }
        }
    }

    fun reloadProfileImageIfNull() {
        if (_uiState.value.profileBitmap == null) {
            viewModelScope.launch {
                val uid = _uiState.value.userUid ?: getUidUseCase().first()
                uid?.let {
                    val bitmap = downloadAndSaveUseCase(it)
                    _uiState.update { it.copy(profileBitmap = bitmap) }
                }
            }
        }
    }

    fun uploadImage(uri: Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
        viewModelScope.launch {
            val currentUid = _uiState.value.userUid ?: getUidUseCase().first { it != null }

            if (currentUid == null) {
                _uiState.update { it.copy(uploadState = Resource.Error("Couldn't find userId.")) }
                return@launch
            }

            _uiState.update { it.copy(uploadState = Resource.Loading) }
            val imageBytes = uriToByteArray(uri)

            if (imageBytes != null) {
                // Update local bitmap immediately for better UX
                val tempBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                _uiState.update { it.copy(profileBitmap = tempBitmap, uploadState = Resource.Loading) }

                uploadProfileUseCase(imageBytes).collect { result ->
                    _uiState.update { it.copy(uploadState = result) }

                    if (result is Resource.Success) {
                        saveProfileImageUrlUseCase(result.data)
                        val newBitmap = downloadAndSaveUseCase(currentUid)
                        _uiState.update { it.copy(
                            profileBitmap = newBitmap,
                            userProfile = it.userProfile?.copy(profileImageUrl = result.data)
                        ) }
                    }
                }
            } else {
                _uiState.update { it.copy(uploadState = Resource.Error("Failed to upload image file.")) }
            }
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            println("ProfileViewModel Error: ${e.message}")
            null
        }
    }

    fun clearUploadState() {
        _uiState.update { it.copy(uploadState = null) }
    }

    fun changePassword() {
        val state = _uiState.value
        viewModelScope.launch {
            changePasswordUseCase(state.oldPassword, state.newPassword).collect { result ->
                _uiState.update { it.copy(changePasswordState = result) }
            }
        }
    }

    fun onOldPasswordChange(newValue: String) {
        _uiState.update { it.copy(oldPassword = newValue) }
    }

    fun onNewPasswordChange(newValue: String) {
        _uiState.update { it.copy(newPassword = newValue) }
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue) }
    }

    fun clearChangePasswordState() {
        _uiState.update { it.copy(changePasswordState = null, oldPassword = "", newPassword = "", confirmPassword = "") }
    }
    fun onBackClicked() {
        navigationDispatcher.navigateBack()
    }

    fun onNavigateTo(destination: ProfileNavigation) {
        navigationDispatcher.navigateTo(destination.route)
    }
}
