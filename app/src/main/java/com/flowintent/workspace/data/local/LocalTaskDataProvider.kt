package com.flowintent.workspace.data.local

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArtTrack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.ui.graphics.vector.ImageVector
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType

object LocalTaskDataProvider {

    val allTasks = listOf(
        Task(
            title = "Gym",
            content = TaskRes.TaskContent("Ideal space for strength, endurance, and fitness."),
            icon = Icons.Default.SportsGymnastics,
            cardColor = "ff56b0a6".toLong(16).toInt(),
            iconColor = "ffffffff".toLong(16).toInt(),
            textColor = "ffffffff".toLong(16).toInt()
        ),
        Task(
            title = "Art",
            content = TaskRes.TaskContent("Unique world where creativity meets vibrant colors."),
            icon = Icons.Default.ArtTrack,
            cardColor = "ff8e70c8".toLong(16).toInt(),
            iconColor = "ffffffff".toLong(16).toInt(),
            textColor = "ffffffff".toLong(16).toInt()
        ),
        Task(
            title = "Sent",
            content = TaskRes.TaskContent("Tasks delivered swiftly, connecting ideas effortlessly."),
            icon = Icons.AutoMirrored.Filled.Send,
            cardColor = "ffffffff".toLong(16).toInt(),
            iconColor = "ff000000".toLong(16).toInt(),
            textColor = "ff000000".toLong(16).toInt()
        ),
        Task(
            title = "Health",
            content = TaskRes.TaskContent("Holistic well-being for body and mind."),
            icon = Icons.Default.HealthAndSafety,
            cardColor = "fff5c66d".toLong(16).toInt(),
            iconColor = "ffffffff".toLong(16).toInt(),
            textColor = "ffffffff".toLong(16).toInt()
        )
    )

    val defaultTask = Task(
        title = "Unknown",
        content = TaskRes.TaskContent("Unknown"),
        icon = Icons.Default.Error,
        cardColor = -1,
        iconColor = -1,
        textColor = -1
    )
}

data class Task(
    val title: String = "",
    var content: TaskRes,
    var icon: ImageVector,
    var taskType: TaskType = TaskType.LOCAL_TASKS,
    var cardColor: Int,
    var iconColor: Int,
    var textColor: Int = -1
)
