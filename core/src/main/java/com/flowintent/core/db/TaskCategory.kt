package com.flowintent.core.db

data class TaskCategory(
    val title: String,
    val content: TaskContent,
    val icon: TaskIcon,
    val cardColor: Long,
    val iconColor: Long,
    val textColor: Long
)

data class TaskContent(
    val text: String
)

data class TaskIcon(
    val type: String,
    val name: String
)
