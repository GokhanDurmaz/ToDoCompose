package com.flowintent.workspace.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getRelativeDayLabel(dueDateMillis: Long): String {
    val now = Calendar.getInstance()
    val due = Calendar.getInstance().apply { timeInMillis = dueDateMillis }

    val today = now.get(Calendar.DAY_OF_YEAR)
    val dueDay = due.get(Calendar.DAY_OF_YEAR)
    val diff = dueDay - today

    return when (diff) {
        0 -> "Today"
        1 -> "Tomorrow"
        -1 -> "Yesterday"
        else -> {
            val format = SimpleDateFormat("dd MMM", Locale.getDefault())
            format.format(due.time)
        }
    }
}
