/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
        val triggerAtMillis = task.dueDate - (30 * 60 * 1000)

        val finalTriggerAt = if (triggerAtMillis > System.currentTimeMillis()) {
            triggerAtMillis
        } else if (task.dueDate > System.currentTimeMillis()) {
            System.currentTimeMillis()
        } else {
            null
        }

        finalTriggerAt?.let {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                it,
                pendingIntent
            )
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
}
