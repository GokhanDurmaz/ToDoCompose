package com.flowintent.profile.ui.vm

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.ChangePasswordUseCase
import com.flowintent.core.db.profile.UploadProfileUseCase
import com.flowintent.core.db.model.UserProfile
import com.flowintent.core.db.profile.DownloadAndSaveUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.db.repository.SupaBaseRepository
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navigationDispatcher: NavigationDispatcher,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val uploadProfileUseCase: UploadProfileUseCase,
    private val downloadAndSaveUseCase: DownloadAndSaveUseCase,
    private val supaBaseRepository: SupaBaseRepository,
    private val encryptedProtoRepository: EncryptedProtoRepository
): ViewModel() {

    private val _changePasswordState = MutableStateFlow<Resource<Unit>?>(null)
    val changePasswordState = _changePasswordState.asStateFlow()

    private val _uploadState = MutableStateFlow<Resource<String>?>(null)
    val uploadState = _uploadState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _profileBitmap = MutableStateFlow<Bitmap?>(null)
    val profileBitmap = _profileBitmap.asStateFlow()

    val userUid: StateFlow<String?> = encryptedProtoRepository.uidFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000),
            initialValue = null
        )

    init {
        observeBitmapLoading()
    }

    private fun observeBitmapLoading() {
        viewModelScope.launch {
            userUid.collectLatest { uid ->
                if (uid != null) {
                    val bitmap = downloadAndSaveUseCase(uid)
                    _profileBitmap.value = bitmap
                }
            }
        }
    }

    fun reloadProfileImageIfNull() {
        if (_profileBitmap.value == null) {
            viewModelScope.launch {
                val uid = userUid.value ?: encryptedProtoRepository.uidFlow().first()
                uid?.let {
                    val bitmap = downloadAndSaveUseCase(it)
                    _profileBitmap.value = bitmap
                }
            }
        }
    }

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val currentUid = userUid.value ?: encryptedProtoRepository.uidFlow().first { it != null }

            if (currentUid == null) {
                _uploadState.value = Resource.Error("Couldn't find userId.")
                return@launch
            }

            _uploadState.value = Resource.Loading
            val imageBytes = uriToByteArray(uri)

            if (imageBytes != null) {
                uploadProfileUseCase(imageBytes).collect { result ->
                    _uploadState.value = result

                    if (result is Resource.Success) {
                        supaBaseRepository.clearLocalAvatar(currentUid)

                        val newBitmap = downloadAndSaveUseCase(currentUid)
                        _profileBitmap.value = newBitmap
                        Log.d("UI_DEBUG", "Bitmap updated: ${newBitmap != null}")
                        _userProfile.value = _userProfile.value?.copy(profileImageUrl = result.data)
                    }
                }
            } else {
                _uploadState.value = Resource.Error("Failed to upload image file.")
            }
        }
    }

    private fun uriToByteArray(uri: Uri): ByteArray? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            Log.e("ProfileViewModel", e.message.toString())
            null
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
