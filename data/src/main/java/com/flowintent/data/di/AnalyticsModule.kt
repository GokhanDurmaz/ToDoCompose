package com.flowintent.data.di

import com.flowintent.core.network.services.AnalyticsService
import com.flowintent.data.db.remote.AnalyticsServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal abstract class AnalyticsModule {
    @Binds
    internal abstract fun bindAnalyticService(analyticsServiceImpl: AnalyticsServiceImpl): AnalyticsService
}