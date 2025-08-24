package com.flowintent.workspace.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.flowintent.core.db.TaskRes

@Composable
@ReadOnlyComposable
fun TaskRes.asString(): String {
    return when(this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> stringResource(id)
    }
}
