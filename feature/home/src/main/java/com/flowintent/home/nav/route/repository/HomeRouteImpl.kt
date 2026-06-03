/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.nav.route.repository

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.flowintent.home.ui.ArtScreen
import com.flowintent.home.ui.GymScreen
import com.flowintent.home.ui.HealthScreen
import com.flowintent.home.ui.HomeScreen
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.nav.MainNavigation
import javax.inject.Inject

/**
 * Implementation of FeatureApi for the Home module.
 */
class HomeRouteImpl @Inject constructor() : FeatureApi {
    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.composable(MainNavigation.HOME.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    when (category) {
                        "Gym" -> navController.navigate(MainNavigation.GYM.route)
                        "Art" -> navController.navigate(MainNavigation.ART.route)
                        "Health" -> navController.navigate(MainNavigation.HEALTH.route)
                    }
                }
            )
        }
        navGraphBuilder.composable(MainNavigation.GYM.route) {
            GymScreen(onBack = { navController.popBackStack() })
        }
        navGraphBuilder.composable(MainNavigation.ART.route) {
            ArtScreen(onBack = { navController.popBackStack() })
        }
        navGraphBuilder.composable(MainNavigation.HEALTH.route) {
            HealthScreen(onBack = { navController.popBackStack() })
        }
    }
}
