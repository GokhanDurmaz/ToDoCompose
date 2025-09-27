package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.flowintent.workspace.ui.ToDoListScreen
import com.flowintent.workspace.ui.MainScreen
import com.flowintent.workspace.ui.ReminderScreen
import com.flowintent.workspace.ui.SettingsScreen

@Composable
fun ToDoNavigationBar(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val startDestination = Navigation.HOME

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = { BottomNavigationBar(currentDestination, navController) }
    ) { contentPadding ->
        AppNavHost(
            navController,
            startDestination.route,
            modifier = Modifier.padding(contentPadding),
            windowSize = windowSize
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    toDoNavigation: String,
    modifier: Modifier,
    windowSize: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = toDoNavigation,
        modifier = modifier
    ) {
        Navigation.entries.forEach { navigation ->
            composable(navigation.route) {
                when(navigation) {
                    Navigation.HOME -> { MainScreen(windowSize) }
                    Navigation.LIST_TODO -> ToDoListScreen()
                    Navigation.REMINDER -> ReminderScreen()
                    Navigation.SETTINGS -> SettingsScreen()
                }
            }
        }
    }
}
