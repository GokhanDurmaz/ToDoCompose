package com.flowintent.workspace.ui.vm

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedType = MutableStateFlow<TaskType?>(null)
    val selectedType = _selectedType.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val tasks: StateFlow<PagingData<Task>> = combine(
        _searchQuery.debounce(300),
        _selectedType
    ) { query, type ->
        query to type
    }.flatMapLatest { (query, type) ->
        repository.getTasks(query, type)
    }.cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )

    fun onTypeSelected(type: TaskType?) {
        _selectedType.value = type
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
    }

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            val idsToDelete = selectedTasks.filter { it.value }.keys.toList()

            idsToDelete.forEach { id ->
                repository.deleteTaskById(id)
            }

            selectedTasks.clear()
            setSelectionMode(false)
        }
    }

    private val _updateTaskId = MutableStateFlow<Int?>(null)
    val updateTaskId: StateFlow<Int?> = _updateTaskId.asStateFlow()

    private val _expandedMap = mutableStateMapOf<Int, Boolean>()
    val expandedMap: Map<Int, Boolean> get() = _expandedMap


    private var _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    val selectedTasks = mutableStateMapOf<Int, Boolean>()

    val selectedCount: Int
        get() = selectedTasks.count { it.value }


    private val _smartTaskState = MutableStateFlow<Resource<Unit>?>(null)
    val smartTaskState: StateFlow<Resource<Unit>?> = _smartTaskState.asStateFlow()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun insertSmartTask(userInput: String) {
        if (userInput.isBlank()) return

        viewModelScope.launch {
            repository.insertSmartTask(userInput).collect { resource ->
                _smartTaskState.value = resource
            }
        }
    }

    fun clearSmartTaskState() {
        _smartTaskState.value = null
    }

    fun updateTask(id: Int, title: String, content: TaskRes) {
        viewModelScope.launch {
            repository.updateTask(id, title, content)
        }
    }

    fun setSelectionMode(enabled: Boolean) {
        _isSelectionMode.value = enabled
        if (!enabled) {
            selectedTasks.clear()
        }
    }

    fun toggleExpanded(id: Int) {
        _expandedMap[id] = !(_expandedMap[id] ?: false)
    }

    fun toggleSelection(uid: Int) {
        val isSelected = !(selectedTasks[uid] ?: false)
        selectedTasks[uid] = isSelected

        if (isSelected && !_isSelectionMode.value) {
            _isSelectionMode.value = true
        }
    }

    fun unselectAll() {
        selectedTasks.keys.forEach { selectedTasks[it] = false }
    }
}
