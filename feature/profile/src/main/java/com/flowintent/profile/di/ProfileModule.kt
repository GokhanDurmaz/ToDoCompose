package com.flowintent.profile.di

import com.flowintent.navigation.FeatureApi
import com.flowintent.profile.nav.route.repository.ProfileRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProfileModule {
    @Binds
    @IntoSet
    @Singleton
    fun bindsProfileFeature(impl: ProfileRouteImpl): FeatureApi
}
