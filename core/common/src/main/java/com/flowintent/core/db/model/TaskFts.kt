package com.flowintent.core.db.model

import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Task::class)
@Entity(tableName = "tasks_fts")
data class TaskFts(
    val title: String,
    val content: String
)
