package com.flowintent.core.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo("title") val title: String = "",
    @ColumnInfo("content") var content: TaskRes,
    @ColumnInfo("task_type") var taskType: TaskType = TaskType.OTHER,
    @ColumnInfo("card_color") var cardColor: Int,
    @ColumnInfo("icon_color") var iconColor: Int,
    @ColumnInfo("text_color") var textColor: Int = -1,
    @ColumnInfo("due_date") var dueDate: Long
)
