package com.flowintent.workspace.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController

@Composable
fun BottomNavigationBar(currentDestination: NavDestination?, navController: NavHostController) {
    val tabs = MainNavigation.entries

    NavigationBar {
        tabs.forEach { navigation ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == navigation.route } == true

            NavigationBarItem(
                selected = isSelected,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(route = navigation.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navigation.icon,
                        contentDescription = stringResource(navigation.contentDescriptionRes)
                    )
                },
                label = {
                    Text(
                        text = stringResource(navigation.labelRes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    }
}
