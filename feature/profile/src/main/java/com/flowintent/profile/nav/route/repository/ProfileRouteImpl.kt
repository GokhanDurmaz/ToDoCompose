/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.nav.route.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.flowintent.auth.ui.TwoFactorScreen
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.nav.ProfileNavigation
import com.flowintent.profile.ui.ChangePasswordScreen
import com.flowintent.profile.ui.EditProfileImageScreen
import com.flowintent.profile.ui.PendingTasksScreen
import com.flowintent.profile.ui.ProfileScreen
import javax.inject.Inject

class ProfileRouteImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable(ProfileNavigation.PROFILE_MAIN.route) {
            ProfileScreen()
        }

        navGraphBuilder.composable(ProfileNavigation.PENDING_TASKS.route) {
            PendingTasksScreen()
        }

        navGraphBuilder.composable(ProfileNavigation.EDIT_PROFILE.route) {
            EditProfileImageScreen()
        }

        navGraphBuilder.composable(ProfileNavigation.CHANGE_PASSWORD.route) {
            ChangePasswordScreen()
        }

        navGraphBuilder.composable(ProfileNavigation.TWO_FACTOR_AUTH.route) {
            TwoFactorScreen()
        }
    }
}
