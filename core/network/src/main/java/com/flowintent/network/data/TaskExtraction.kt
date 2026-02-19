package com.flowintent.network.data

import com.flowintent.core.db.TaskType

data class TaskExtraction(
    val title: String,
    val time: String? = null,
    val category: String = TaskType.OTHER.toString()
)
