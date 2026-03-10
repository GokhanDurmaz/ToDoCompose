package com.flowintent.auth.nav.route.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.auth.ui.ForgotPasswordScreen
import com.flowintent.auth.ui.SignInScreen
import com.flowintent.auth.ui.SignUpScreen
import com.flowintent.navigation.FeatureApi
import javax.inject.Inject

class AuthRouteImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable(AuthNavigation.SIGN_UP.route) {
            SignUpScreen()
        }
        navGraphBuilder.composable(AuthNavigation.SIGN_IN.route) {
            SignInScreen()
        }
        navGraphBuilder.composable(AuthNavigation.FORGOT_PASSWORD.route) {
            ForgotPasswordScreen()
        }
    }
}
