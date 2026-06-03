/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.scenarios

import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType

object TaskScenarios {
    fun createSampleTask(
        uid: Int = 1,
        title: String = "Sample Task",
        content: String = "This is a sample task content",
        taskType: TaskType = TaskType.WORK
    ): Task {
        return Task(
            uid = uid,
            title = title,
            content = TaskRes.TaskContent(content),
            dueDate = System.currentTimeMillis(),
            taskType = taskType
        )
    }

    fun createMultipleTasks(count: Int): List<Task> {
        return (1..count).map {
            createSampleTask(uid = it, title = "Task $it")
        }
    }
}
