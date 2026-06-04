/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.notification.TaskNotificationScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var notificationScheduler: TaskNotificationScheduler

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            scope.launch {
                try {
                    val tasks = taskRepository.getAllTasksRaw()
                    tasks.forEach { task ->
                        notificationScheduler.schedule(task)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
