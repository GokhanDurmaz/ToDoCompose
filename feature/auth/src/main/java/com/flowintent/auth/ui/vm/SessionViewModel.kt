/**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.auth.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.GetEmailUseCase
import com.flowintent.core.db.auth.GetNameUseCase
import com.flowintent.core.db.auth.GetProfileImageUrlUseCase
import com.flowintent.core.db.auth.GetTokenUseCase
import com.flowintent.core.db.auth.GetUidUseCase
import com.flowintent.core.db.auth.GetUserProfileUseCase
import com.flowintent.core.db.auth.LogoutUseCase
import com.flowintent.core.db.auth.SaveUserInfoUseCase
import com.flowintent.core.db.auth.UpdateUidUseCase
import com.flowintent.core.util.AppEventTracker
import com.flowintent.core.util.Resource
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.AuthNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getNameUseCase: GetNameUseCase,
    private val getEmailUseCase: GetEmailUseCase,
    private val getProfileImageUrlUseCase: GetProfileImageUrlUseCase,
    private val getUidUseCase: GetUidUseCase,
    private val updateUidUseCase: UpdateUidUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    val token: StateFlow<String?> = getTokenUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val userName: StateFlow<String?> = getNameUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val userEmail: StateFlow<String?> = getEmailUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val profileImageUrl: StateFlow<String?> = getProfileImageUrlUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500.milliseconds)
            _isReady.value = true
        }
    }

    private fun saveUser(firstName: String, lastName: String, email: String) = viewModelScope.launch {
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
                        eventTracker.logEvent("logout_error", mapOf("error" to (resource.message)))
                        eventTracker.logMessage("Logout failed: ${resource.message}")
                    }
                }
            }
        }
    }
}
