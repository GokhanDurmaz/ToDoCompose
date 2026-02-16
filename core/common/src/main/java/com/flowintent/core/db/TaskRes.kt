package com.flowintent.core.db

import androidx.annotation.StringRes

sealed class TaskRes {
    data class TaskContent(val content: String): TaskRes()
    data class TaskContentRes(@StringRes val id: Int): TaskRes()
}
