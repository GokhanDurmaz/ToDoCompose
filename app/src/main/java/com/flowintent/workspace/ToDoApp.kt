 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.flowintent.data.secure.SecurityInterceptor
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

 @HiltAndroidApp
class ToDoApp: Application() {

    @Inject
    lateinit var securityInterceptor: SecurityInterceptor

    override fun onCreate() {
        super.onCreate()
        securityInterceptor.initialize(this)

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminders"
            val descriptionText = "Notifications for upcoming tasks"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("task_reminders", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
