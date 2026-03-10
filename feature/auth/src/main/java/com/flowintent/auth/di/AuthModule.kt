package com.flowintent.auth.di

import com.flowintent.auth.nav.route.repository.AuthRouteImpl
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.navigation.FeatureApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    @IntoSet
    @Singleton
    fun bindAuthFeature(impl: AuthRouteImpl): FeatureApi
}
