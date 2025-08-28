package com.flowintent.workspace.di

import com.flowintent.core.network.services.AnalyticsService
import com.flowintent.workspace.data.remote.AnalyticsServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {
    @Binds
    abstract fun bindAnalyticService(analyticsServiceImpl: AnalyticsServiceImpl): AnalyticsService
}
