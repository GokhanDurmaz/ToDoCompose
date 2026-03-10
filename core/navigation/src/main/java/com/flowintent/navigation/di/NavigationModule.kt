package com.flowintent.navigation.di

import com.flowintent.navigation.NavigationDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigationDispatcher(): NavigationDispatcher = NavigationDispatcher()
}
