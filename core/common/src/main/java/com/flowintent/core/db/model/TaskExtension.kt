package com.flowintent.core.db.model

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

fun TaskRes.toDisplayContent(): String {
    return when (this) {
        is TaskRes.TaskContent -> {
            println("Debug: TaskContent formatında veri: $content")
            content
        }
        is TaskRes.TaskContentRes -> {
            val str = stringResource(id)
            println("Debug: TaskContentRes formatında veri: $str")
            str
        }
    }.also { if (it.isBlank()) println("UYARI: asString() boş bir string döndürdü!") }
}

fun TaskRes.toDisplayContentNonComposable(context: Context): String {
    return when (this) {
        is TaskRes.TaskContent -> content
        is TaskRes.TaskContentRes -> context.getString(id)
    }
}
