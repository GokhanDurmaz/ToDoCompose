/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun TaskRes.toDisplayContent(): String {
    return when (this) {
        is TaskRes.TaskContent -> {
            println("Debug: TaskContent formatted data: $content")
            content
        }
        is TaskRes.TaskContentRes -> {
            val str = stringResource(id)
            println("Debug: TaskContentRes formatted data: $str")
            str
        }
    }.also { if (it.isBlank()) println("WARNING: asString() returned an empty string!") }
}

fun TaskRes.toDisplayContentNonComposable(context: Context): String {
    return when (this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> context.getString(id)
    }
}
