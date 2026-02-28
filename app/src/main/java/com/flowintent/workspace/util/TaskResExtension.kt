package com.flowintent.workspace.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.flowintent.core.db.model.TaskRes

@Composable
fun TaskRes.asString(): String {
    return when (this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> stringResource(id)
    }
}

fun TaskRes.asStringNonComposable(context: Context): String {
    return when (this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> context.getString(id)
    }
}
