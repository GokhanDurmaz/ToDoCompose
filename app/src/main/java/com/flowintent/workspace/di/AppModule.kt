package com.flowintent.workspace.di

import com.flowintent.navigation.FeatureApi
import com.flowintent.workspace.nav.route.repository.MainRouteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {
    @Binds
    @IntoSet
    @Singleton
    internal abstract fun bindMainFeature(impl: MainRouteImpl): FeatureApi
}
