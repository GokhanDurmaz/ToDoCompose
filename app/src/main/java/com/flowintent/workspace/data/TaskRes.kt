package com.flowintent.workspace.data

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

sealed class TaskRes {
    data class TaskContent(val content: String): TaskRes()
    data class TaskContentRes(@StringRes val id: Int): TaskRes()
}

@Composable
@ReadOnlyComposable
fun TaskRes.asString(): String {
    return when(this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> stringResource(id)
    }
}