package com.flowintent.workspace.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flowintent.workspace.ui.ToDoListScreen
import com.flowintent.workspace.ui.ToDoScreen

@Composable
fun ToDoNavigationBar(windowSize: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val startDestination = ToDoNavigation.HOME

    Scaffold(
        bottomBar = { BottomNavigationBar(startDestination, navController) }
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
        ToDoNavigation.entries.forEach { navigation ->
            composable(navigation.route) {
                when(navigation) {
                    ToDoNavigation.HOME -> { ToDoScreen(windowSize) }
                    ToDoNavigation.LIST_TODO -> ToDoListScreen()
                    ToDoNavigation.REMINDER -> ReminderScreen()
                    ToDoNavigation.SETTINGS -> SettingsScreen()
                }
            }
        }
    }
}


@Composable
private fun BottomNavigationBar(startDestination: ToDoNavigation, navController: NavHostController) {
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    NavigationBar {
        ToDoNavigation.entries.forEachIndexed { index, navigation ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    navController.navigate(route = navigation.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    selectedDestination = index
                },
                icon = {
                    Icon(
                        imageVector = navigation.icon,
                        contentDescription = navigation.contentDescription
                    )
                },
                label = { Text(text = navigation.label) }
            )
        }
    }
}

@Composable
fun ReminderScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Reminder",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings",
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}