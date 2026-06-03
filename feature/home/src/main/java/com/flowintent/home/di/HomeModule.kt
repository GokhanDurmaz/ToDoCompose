/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.di

import com.flowintent.home.nav.route.repository.HomeRouteImpl
import com.flowintent.navigation.FeatureApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

/**
 * Hilt module to provide home-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {
    @Binds
    @IntoSet
    @Singleton
    fun bindHomeFeature(impl: HomeRouteImpl): FeatureApi
}
