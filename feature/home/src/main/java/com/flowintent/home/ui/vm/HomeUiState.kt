/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui.vm

import com.flowintent.core.db.model.TaskCategory

/**
 * UI State for the Home screen.
 */
data class HomeUiState(
    val categories: List<TaskCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
