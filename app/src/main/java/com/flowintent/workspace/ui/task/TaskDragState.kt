package com.flowintent.workspace.ui.task

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.Dp
import com.flowintent.core.db.model.DragInfo
import com.flowintent.core.db.model.Task

data class TaskDragState(
    val index: Int,
    val draggingItem: DragInfo?,
    val itemHeight: Dp,
    val filteredList: SnapshotStateList<Task>,
    val onDragUpdate: (DragInfo?) -> Unit,
    val onHeightChange: (Dp) -> Unit
)
