package com.flowintent.navigation

import androidx.navigation.NavOptionsBuilder

sealed class NavigationCommand {
    data class ToRoute(
        val route: Any,
        val options: NavOptionsBuilder.() -> Unit = {}
    ) : NavigationCommand()

    data object Back : NavigationCommand()

    data class BackWithResult<T : Any>(
        val key: String,
        val result: T,
        val destinationRoute: Any? = null
    ) : NavigationCommand()
}
