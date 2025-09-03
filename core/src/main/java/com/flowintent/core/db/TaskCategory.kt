package com.flowintent.core.db

import androidx.annotation.Keep

@Keep
data class TaskCategory(
    val title: String,
    val content: TaskContent,
    val icon: TaskIcon,
    val cardColor: Long,
    val iconColor: Long,
    val textColor: Long
)

@Keep
data class TaskContent(
    val text: String
)

@Keep
data class TaskIcon(
    val type: String,
    val name: String
)
