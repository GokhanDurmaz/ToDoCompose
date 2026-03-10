package com.flowintent.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface FeatureApi {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
    )
}
