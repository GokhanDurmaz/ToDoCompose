/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.notification

import com.flowintent.core.db.model.Task

interface TaskNotificationScheduler {
    fun schedule(task: Task)
    fun cancel(task: Task)
}
