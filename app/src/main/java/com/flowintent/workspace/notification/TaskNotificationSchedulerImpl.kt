/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.notification

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.notification.TaskNotificationScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskNotificationSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskNotificationScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(task: Task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w("TaskNotification", "POST_NOTIFICATIONS permission not granted.")
            }
        }
        Log.d("TaskNotification", "Scheduling task: ${task.title} at ${task.dueDate}")
        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra(TaskNotificationReceiver.EXTRA_TASK_ID, task.uid)
            putExtra(TaskNotificationReceiver.EXTRA_TASK_TITLE, task.title)
            val contentText = when (val content = task.content) {
                is TaskRes.TaskContent -> content.content
                is TaskRes.TaskContentRes -> context.getString(content.id)
            }
            putExtra(TaskNotificationReceiver.EXTRA_TASK_CONTENT, contentText)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.uid,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule for 30 minutes before due date
        val triggerAtMillis = task.dueDate - NOTIFICATION_LEAD_TIME_MILLIS

        val finalTriggerAt = if (triggerAtMillis > System.currentTimeMillis()) {
            triggerAtMillis
        } else if (task.dueDate > System.currentTimeMillis()) {
            System.currentTimeMillis()
        } else {
            null
        }

        finalTriggerAt?.let {
            Log.d("TaskNotification", "Triggering alarm at $it")
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        it,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        it,
                        pendingIntent
                    )
                }
            } catch (e: SecurityException) {
                Log.e("TaskNotification", "SecurityException when scheduling alarm", e)
            }
        }
    }

    override fun cancel(task: Task) {
        val intent = Intent(context, TaskNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.uid,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        private const val MINUTES_TO_MILLIS = 60 * 1000
        private const val LEAD_TIME_MINUTES = 30
        private const val NOTIFICATION_LEAD_TIME_MILLIS = LEAD_TIME_MINUTES * MINUTES_TO_MILLIS
    }
}
