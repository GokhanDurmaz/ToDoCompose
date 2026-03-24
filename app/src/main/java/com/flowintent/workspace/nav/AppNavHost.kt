package com.flowintent.workspace.nav

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flowintent.navigation.nav.AuthNavigation
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.NavigationCommand
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.nav.route.BottomNavigationBar

@Composable
fun ToDoNavigationBar(
    authViewModel: AuthViewModel = hiltViewModel(),
    navigationDispatcher: NavigationDispatcher,
    featureApis: Set<FeatureApi>
) {
    val navController = rememberNavController()
    val token by authViewModel.token.collectAsStateWithLifecycle()
    val isReady by authViewModel.isReady.collectAsStateWithLifecycle()

    NavigationEventHandler(navController, navigationDispatcher)

    if (isReady.not()) {
        return
    }

    val startRoute = remember(isReady) {
        if (token.isNullOrEmpty()) AuthNavigation.SIGN_IN.route else MainNavigation.HOME.route
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomBar = MainNavigation.entries.any { it.route == currentRoute }

    Scaffold { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = startRoute,
                modifier = Modifier.padding(contentPadding)
            ) {
                featureApis.forEach { api ->
                    api.registerGraph(navGraphBuilder = this, navController = navController)
                }
            }

            if (shouldShowBottomBar) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    BottomNavigationBar(
                        currentDestination = navBackStackEntry?.destination,
                        navigationDispatcher = navigationDispatcher,
                        modifier = Modifier
                            .graphicsLayer {
                                shape = RoundedCornerShape(50.dp)
                                clip = true
                            }
                            .shadow(8.dp, RoundedCornerShape(50.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationEventHandler(
    navController: NavHostController,
    navigationDispatcher: NavigationDispatcher
) {
    LaunchedEffect(navController, navigationDispatcher) {
        navigationDispatcher.navigationEvents.collect { command ->
            kotlinx.coroutines.yield()
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
            val cleanRoute = command.route.toString().trim()

            try {
                navController.navigate(cleanRoute) {
                    command.options(this)
                }
            } catch (e: IllegalArgumentException) {
                Log.e("NavError", "Route err: ${e.message}")
            } catch (e: IllegalStateException) {
                Log.e("NavError", "NavGraph is not rendered yet: ${e.message}", e)
            }
        }
        is NavigationCommand.Back -> navController.popBackStack()

        is NavigationCommand.BackWithResult<*> -> {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                command.key,
                command.result
            )
            command.destinationRoute?.let { route ->
                navController.popBackStack(route, inclusive = false)
            } ?: navController.popBackStack()
        }
    }
}
