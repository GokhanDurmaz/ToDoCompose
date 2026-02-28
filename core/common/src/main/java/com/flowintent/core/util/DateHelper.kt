package com.flowintent.core.util

import com.flowintent.core.db.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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

fun parseDateToLong(timeString: String?, existingTasks: List<Task>): Long {
    val cleanTime = timeString?.trim()

    if (cleanTime == null || cleanTime == "null" || cleanTime.isBlank()) {
        val calendar = Calendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= 9) {
                add(Calendar.HOUR_OF_DAY, 1)
                set(Calendar.MINUTE, 0)
            } else {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
            }
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        while (existingTasks.any { it.dueDate == calendar.timeInMillis }) {
            calendar.add(Calendar.MINUTE, 30)
        }
        return calendar.timeInMillis
    }

    val timestamp = cleanTime.toLongOrNull()
    if (timestamp != null) return timestamp

    val formats = listOf("yyyy-MM-dd HH:mm", "yyyy-MM-dd")
    var finalTime: Long? = null

    for (format in formats) {
        try {
            val sdf = SimpleDateFormat(format, Locale.US)
            val date = sdf.parse(cleanTime)

            if (date != null) {
                val calendar = Calendar.getInstance().apply { time = date }

                if (format == "yyyy-MM-dd") {
                    calendar.set(Calendar.HOUR_OF_DAY, 9)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                }

                while (existingTasks.any { it.dueDate == calendar.timeInMillis }) {
                    calendar.add(Calendar.MINUTE, 30)
                }
                finalTime = calendar.timeInMillis
                break
            }
        } catch (e: Exception) { continue }
    }

    return finalTime ?: System.currentTimeMillis()
}

fun Long.toReadableDateTime(): String {
    val date = Date(this)
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(date)
}
