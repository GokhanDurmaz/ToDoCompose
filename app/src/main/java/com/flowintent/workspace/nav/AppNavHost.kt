package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flowintent.workspace.ui.AdvancedSettingsScreen
import com.flowintent.workspace.ui.ForgotPasswordScreen
import com.flowintent.workspace.ui.ToDoListScreen
import com.flowintent.workspace.ui.MainScreen
import com.flowintent.workspace.ui.ReminderScreen
import com.flowintent.workspace.ui.SignInScreen
import com.flowintent.workspace.ui.SignUpScreen
import com.flowintent.workspace.ui.vm.AuthViewModel

@Composable
fun ToDoNavigationBar(windowSize: WindowWidthSizeClass, authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val token by authViewModel.token.collectAsStateWithLifecycle()
    val isReady by authViewModel.isReady.collectAsStateWithLifecycle()

    if (!isReady) {
        return
    }

    val startRoute = if (token.isNullOrEmpty()) {
        AuthNavigation.SIGN_IN.route
    } else {
        MainNavigation.HOME.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = MainNavigation.entries.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(navBackStackEntry?.destination, navController)
            }
        }
    ) { contentPadding ->
        AppNavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier.padding(contentPadding),
            windowSize = windowSize,
            authViewModel = authViewModel
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier,
    windowSize: WindowWidthSizeClass,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(AuthNavigation.SIGN_IN.route) {
            SignInScreen(
                viewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(AuthNavigation.SIGN_UP.route)
                },
                onSuccessLogin = {
                    navController.navigate(MainNavigation.HOME.route) {
                        popUpTo(AuthNavigation.SIGN_IN.route) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthNavigation.FORGOT_PASSWORD.route)
                }
            )
        }

        composable(AuthNavigation.FORGOT_PASSWORD.route) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AuthNavigation.SIGN_UP.route) {
            SignUpScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSuccessRegistration = {
                    navController.popBackStack()
                }
            )
        }

        MainNavigation.entries.forEach { navigation ->
            composable(navigation.route) {
                when(navigation) {
                    MainNavigation.HOME -> MainScreen(windowSize)
                    MainNavigation.LIST_TODO -> ToDoListScreen()
                    MainNavigation.REMINDER -> ReminderScreen()
                    MainNavigation.SETTINGS -> AdvancedSettingsScreen(viewModel = authViewModel)
                }
            }
        }
    }
}
