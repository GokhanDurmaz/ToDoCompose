package com.flowintent.settings.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.ProfileNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
): ViewModel() {
    fun onProfileClicked() {
        navigationDispatcher.navigateTo(ProfileNavigation.PROFILE_MAIN.route)
    }
}
