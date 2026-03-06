package com.flowintent.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() {

    private val _navigationEvents = Channel<NavigationCommand>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun navigateTo(
        route: Any,
        options: NavOptionsBuilder.() -> Unit = {}
    ) {
        _navigationEvents.trySend(NavigationCommand.ToRoute(route, options))
    }

    fun navigateBack() {
        _navigationEvents.trySend(NavigationCommand.Back)
    }

    /**
     * @param key For identifying data (e.g: "user_id")
     * @param result The object data
     * @param destinationRoute The object route
     */
    fun <T : Any> navigateBackWithResult(
        key: String, 
        result: T, 
        destinationRoute: Any? = null
    ) {
        _navigationEvents.trySend(
            NavigationCommand.BackWithResult(key, result, destinationRoute)
        )
    }
}
