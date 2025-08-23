package com.flowintent.workspace.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flowintent.workspace.data.TaskRes
import com.flowintent.workspace.data.TaskType

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo("name") val name: String = "",
    @ColumnInfo("content") var content: TaskRes,
    @ColumnInfo("task_type") var taskType: TaskType = TaskType.LOCAL_TASKS
)
