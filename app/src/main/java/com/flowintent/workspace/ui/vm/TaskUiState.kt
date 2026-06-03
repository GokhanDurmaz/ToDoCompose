/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui.vm

import com.flowintent.core.db.model.TaskType
import com.flowintent.core.util.Resource

data class TaskUiState(
    val searchQuery: String = "",
    val selectedType: TaskType? = null,
    val isSelectionMode: Boolean = false,
    val selectedTasks: Map<Int, Boolean> = emptyMap(),
    val expandedTasks: Map<Int, Boolean> = emptyMap(),
    val smartTaskState: Resource<Unit>? = null,
    val updateTaskId: Int? = null
) {
    val selectedCount: Int get() = selectedTasks.count { it.value }
}
