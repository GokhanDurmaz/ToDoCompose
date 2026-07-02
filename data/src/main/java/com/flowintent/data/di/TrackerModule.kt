/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.data.di

import com.flowintent.core.util.AppEventTracker
import com.flowintent.data.tracker.AppEventTrackerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerModule {

    @Binds
    @Singleton
    abstract fun bindAppEventTracker(
        appEventTrackerImpl: AppEventTrackerImpl
    ): AppEventTracker
}
