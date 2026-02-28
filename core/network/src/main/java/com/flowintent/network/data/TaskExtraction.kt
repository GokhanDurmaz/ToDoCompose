package com.flowintent.network.data

import androidx.annotation.Keep
import com.flowintent.core.db.model.ActionType
import com.flowintent.core.db.model.TaskType

@Keep
data class TaskExtraction(
    val title: String = "",
    val time: String? = null,
    val category: String = TaskType.OTHER.toString(),
    val action: String = ActionType.ADD.toString()
)
