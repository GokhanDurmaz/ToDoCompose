package com.flowintent.workspace.nav

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flowintent.navigation.NavigationCommand
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.ui.AdvancedSettingsScreen
import com.flowintent.workspace.ui.ForgotPasswordScreen
import com.flowintent.workspace.ui.ToDoListScreen
import com.flowintent.workspace.ui.MainScreen
import com.flowintent.workspace.ui.ReminderScreen
import com.flowintent.workspace.ui.SignInScreen
import com.flowintent.workspace.ui.SignUpScreen
import com.flowintent.workspace.ui.vm.AuthViewModel

@Composable
fun ToDoNavigationBar(
    windowSize: WindowWidthSizeClass,
    authViewModel: AuthViewModel = hiltViewModel(),
    navigationDispatcher: NavigationDispatcher
) {
    val navController = rememberNavController()
    val token by authViewModel.token.collectAsStateWithLifecycle()
    val isReady by authViewModel.isReady.collectAsStateWithLifecycle()

    NavigationEventHandler(navController, navigationDispatcher)

    if (!isReady) return

    val startRoute = if (token.isNullOrEmpty()) AuthNavigation.SIGN_IN.route
    else MainNavigation.HOME.route

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = MainNavigation.entries.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    currentDestination = navBackStackEntry?.destination,
                    navigationDispatcher = navigationDispatcher
                )
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
private fun NavigationEventHandler(
    navController: NavHostController,
    navigationDispatcher: NavigationDispatcher
) {
    LaunchedEffect(navController) {
        navigationDispatcher.navigationEvents.collect { command ->
            handleNavigationCommand(command, navController)
        }
    }
}

private fun handleNavigationCommand(
    command: NavigationCommand,
    navController: NavHostController
) {
    when (command) {
        is NavigationCommand.ToRoute -> {
            if (isNavGraphReady(navController).not()) return

            try {
                navController.navigate(command.route.toString(), command.options)
            } catch (e: IllegalArgumentException) {
                Log.e("ToDoNavigationBar", "Navigasyon rotası bulunamadı: ${command.route}", e)
            } catch (e: IllegalStateException) {
                Log.e("ToDoNavigationBar", "NavController geçersiz durumda: ${e.message}", e)
            }
        }
        is NavigationCommand.Back -> navController.popBackStack()
        is NavigationCommand.BackWithResult<*> -> handleBackWithResult(command, navController)
    }
}

private fun isNavGraphReady(navController: NavHostController): Boolean {
    return try {
        navController.graph
        true
    } catch (e: IllegalStateException) {
        Log.w("ToDoNavigationBar", "Graph not ready yet: ${e.message}")
        false
    }
}

private fun handleBackWithResult(
    command: NavigationCommand.BackWithResult<*>,
    navController: NavHostController
) {
    navController.previousBackStackEntry?.savedStateHandle?.set(command.key, command.result)
    command.destinationRoute?.let { route ->
        navController.popBackStack(route, inclusive = false)
    } ?: navController.popBackStack()
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
            SignInScreen(viewModel = authViewModel)
        }

        composable(AuthNavigation.FORGOT_PASSWORD.route) {
            ForgotPasswordScreen(viewModel = authViewModel)
        }

        composable(AuthNavigation.SIGN_UP.route) {
            SignUpScreen(viewModel = authViewModel)
        }

        MainNavigation.entries.forEach { navigation ->
            println("NavGraph: Adding route ${navigation.route}")
            composable(route = navigation.route) {
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
