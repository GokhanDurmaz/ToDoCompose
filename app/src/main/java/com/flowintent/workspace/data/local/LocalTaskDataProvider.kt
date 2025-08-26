package com.flowintent.workspace.data.local

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes

object LocalTaskDataProvider {

    val allTasks = listOf(
        listOf(
            Task(
                title = "Gym",
                content = TaskRes.TaskContent("Ideal space for strength, endurance, and fitness."),
                cardColor = "ff56b0a6".toLong(16).toInt(),
                iconColor = "ffffffff".toLong(16).toInt(),
                textColor = "ffffffff".toLong(16).toInt()
            ),
            Task(
                title = "Art",
                content = TaskRes.TaskContent("Unique world where creativity meets vibrant colors."),
                cardColor = "ff8e70c8".toLong(16).toInt(),
                iconColor = "ffffffff".toLong(16).toInt(),
                textColor = "ffffffff".toLong(16).toInt()
            ),
        ),
        listOf(
            Task(
                title = "Sent",
                content = TaskRes.TaskContent("Tasks delivered swiftly, connecting ideas effortlessly."),
                cardColor = "ffffffff".toLong(16).toInt(),
                iconColor = "ff000000".toLong(16).toInt(),
                textColor = "ff000000".toLong(16).toInt()
            ),
            Task(
                title = "Health",
                content = TaskRes.TaskContent("Holistic well-being for body and mind."),
                cardColor = "fff5c66d".toLong(16).toInt(),
                iconColor = "ffffffff".toLong(16).toInt(),
                textColor = "ffffffff".toLong(16).toInt()
            )
        )
    )

    val defaultTask = Task(
        title = "Unknown",
        content = TaskRes.TaskContent("Unknown"),
        cardColor = -1,
        iconColor = -1,
        textColor = -1
    )
}
