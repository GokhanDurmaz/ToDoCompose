/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.di

import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module to provide home-related use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object HomeUseCaseModule {

    @Provides
    @Singleton
    fun provideGetHomeCategoriesUseCase(): GetHomeCategoriesUseCase {
        return GetHomeCategoriesUseCase()
    }
}
