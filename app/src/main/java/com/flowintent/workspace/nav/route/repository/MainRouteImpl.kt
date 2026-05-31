package com.flowintent.workspace.nav.route.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.profile.ui.PendingTasksScreen
import com.flowintent.settings.ui.AdvancedSettingsScreen
import com.flowintent.workspace.ui.MainScreen
import com.flowintent.workspace.ui.ToDoListScreen
import javax.inject.Inject

class MainRouteImpl @Inject constructor(
    private val navigationDispatcher: NavigationDispatcher
): FeatureApi {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable(MainNavigation.HOME.route) {
            MainScreen(navigationDispatcher = navigationDispatcher)
        }
        navGraphBuilder.composable(MainNavigation.LIST_TODO.route) {
            ToDoListScreen()
        }
        navGraphBuilder.composable(MainNavigation.PENDING.route) {
            PendingTasksScreen()
        }
        navGraphBuilder.composable(MainNavigation.SETTINGS.route) {
            AdvancedSettingsScreen()
        }
    }
}
