/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.task.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PendingTasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendingTasksUiState())
    val uiState: StateFlow<PendingTasksUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: Flow<PagingData<Task>> = _uiState
        .flatMapLatest { state ->
            getTasksUseCase(state.searchQuery, state.selectedType)
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTypeSelected(type: TaskType?) {
        _uiState.update { it.copy(selectedType = type) }
    }
}
