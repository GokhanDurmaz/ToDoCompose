package com.flowintent.workspace.data

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

sealed class SongRes {
    data class SongContent(val content: String): SongRes()
    data class SongContentRes(@StringRes val id: Int): SongRes()
}

@Composable
@ReadOnlyComposable
fun SongRes.asString(): String {
    return when(this) {
        is SongRes.SongContent -> content
        is SongRes.SongContentRes -> stringResource(id)
    }
}