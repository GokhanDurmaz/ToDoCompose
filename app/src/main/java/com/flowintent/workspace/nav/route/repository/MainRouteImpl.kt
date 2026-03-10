package com.flowintent.workspace.nav.route.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.settings.ui.AdvancedSettingsScreen
import com.flowintent.workspace.ui.MainScreen
import com.flowintent.workspace.ui.ReminderScreen
import com.flowintent.workspace.ui.ToDoListScreen
import javax.inject.Inject

class MainRouteImpl @Inject constructor(): FeatureApi {
    override fun registerGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable(MainNavigation.HOME.route) {
            MainScreen()
        }
        navGraphBuilder.composable(MainNavigation.LIST_TODO.route) {
            ToDoListScreen()
        }
        navGraphBuilder.composable(MainNavigation.REMINDER.route) {
            ReminderScreen()
        }
        navGraphBuilder.composable(MainNavigation.SETTINGS.route) {
            AdvancedSettingsScreen()
        }
    }
}
