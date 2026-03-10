package com.flowintent.workspace.nav.route

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.navigation.nav.MainNavigation
import com.flowintent.workspace.ui.bottombar.bottomNavItems

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    navigationDispatcher: NavigationDispatcher
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.navigation.route } == true

            NavigationBarItem(
                selected = isSelected,
                alwaysShowLabel = false,
                onClick = {
                    navigationDispatcher.navigateTo(item.navigation.route) {
                        popUpTo(MainNavigation.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.contentDescriptionRes)
                    )
                },
                label = {
                    Text(
                        text = stringResource(item.labelRes),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}
