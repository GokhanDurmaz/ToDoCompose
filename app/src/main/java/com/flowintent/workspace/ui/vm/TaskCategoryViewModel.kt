/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskCategoryViewModel @Inject constructor(
    getHomeCategoriesUseCase: GetHomeCategoriesUseCase
): ViewModel() {
    val allCategories: StateFlow<Resource<List<TaskCategory>>> = getHomeCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )
}
