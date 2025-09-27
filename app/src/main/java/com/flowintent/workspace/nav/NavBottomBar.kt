package com.flowintent.workspace.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(currentDestination: NavDestination?, navController: NavHostController) {
    val tabs = Navigation.entries

    NavigationBar {
        tabs.forEachIndexed { index, navigation ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == navigation.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(route = navigation.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                        restoreState = true
                    }
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
