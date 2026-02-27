package com.flowintent.network.data

import androidx.annotation.Keep
import com.flowintent.core.db.TaskType

@Keep
data class TaskExtraction(
    val title: String = "",
    val time: String? = null,
    val category: String = TaskType.OTHER.toString()
)
