/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.di

import com.flowintent.core.notification.TaskNotificationScheduler
import com.flowintent.workspace.notification.TaskNotificationSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindTaskNotificationScheduler(
        impl: TaskNotificationSchedulerImpl
    ): TaskNotificationScheduler
}
