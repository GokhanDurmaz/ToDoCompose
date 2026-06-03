/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val tasks: StateFlow<PagingData<Task>> = _uiState
        .map { it.searchQuery to it.selectedType }
        .debounce(300)
        .flatMapLatest { (query, type) ->
            repository.getTasks(query, type)
        }.cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )

    fun onTypeSelected(type: TaskType?) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            val idsToDelete = _uiState.value.selectedTasks.filter { it.value }.keys.toList()

            idsToDelete.forEach { id ->
                repository.deleteTaskById(id)
            }

            _uiState.update { it.copy(selectedTasks = emptyMap(), isSelectionMode = false) }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun insertSmartTask(userInput: String) {
        if (userInput.isBlank()) return

        viewModelScope.launch {
            repository.insertSmartTask(userInput).collect { resource ->
                _uiState.update { it.copy(smartTaskState = resource) }
            }
        }
    }

    fun clearSmartTaskState() {
        _uiState.update { it.copy(smartTaskState = null) }
    }

    fun updateTask(id: Int, title: String, content: TaskRes) {
        viewModelScope.launch {
            repository.updateTask(id, title, content)
        }
    }

    fun setSelectionMode(enabled: Boolean) {
        _uiState.update { 
            it.copy(isSelectionMode = enabled, selectedTasks = if (enabled) it.selectedTasks else emptyMap())
        }
    }

    fun toggleExpanded(id: Int) {
        _uiState.update { 
            val newMap = it.expandedTasks.toMutableMap()
            newMap[id] = !(newMap[id] ?: false)
            it.copy(expandedTasks = newMap)
        }
    }

    fun toggleSelection(uid: Int) {
        _uiState.update { 
            val newMap = it.selectedTasks.toMutableMap()
            val isSelected = !(newMap[uid] ?: false)
            newMap[uid] = isSelected
            
            it.copy(
                selectedTasks = newMap,
                isSelectionMode = it.isSelectionMode || isSelected
            )
        }
    }

    fun unselectAll() {
        _uiState.update { 
            val newMap = it.selectedTasks.mapValues { false }
            it.copy(selectedTasks = newMap)
        }
    }
}
