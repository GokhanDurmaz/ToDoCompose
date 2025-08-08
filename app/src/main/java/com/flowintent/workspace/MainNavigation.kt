package com.flowintent.workspace

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class Profile(val name: String)

@Serializable
object FriendList

@Composable
fun ProfileScreen(profile: Profile, onNavigateToFriendList: () -> Unit) {
    Text("Profile for ${profile.name}")
    Button(onClick = { onNavigateToFriendList }) {
        Text("Go to Friends List")
    }
}

@Composable
fun FriendListScreen(onNavigateToProfile: () -> Unit) {
    Text("Friend List")
    Button(onClick = { onNavigateToProfile }) {
        Text("Go to Profile")
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Profile("Gökhan Durmaz")) {
        composable<Profile> { backStackEntry ->
            val profile: Profile = backStackEntry.toRoute()
            ProfileScreen(profile = profile, onNavigateToFriendList = {
                navController.navigate(route = FriendList)
            })
        }
        composable<FriendList> {
            FriendListScreen(onNavigateToProfile = {
                navController.navigate(
                    route = Profile("Unknown Name")
                )
            })
        }
    }
}