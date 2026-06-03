/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui.vm

import com.flowintent.core.db.model.TaskType

data class PendingTasksUiState(
    val searchQuery: String = "",
    val selectedType: TaskType? = null,
    val isLoading: Boolean = false
)
