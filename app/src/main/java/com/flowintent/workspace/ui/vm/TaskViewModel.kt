package com.flowintent.workspace.ui.vm

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.source.ITaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: ITaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _updateTaskId = MutableStateFlow<Int?>(null)
    val updateTaskId: StateFlow<Int?> = _updateTaskId.asStateFlow()

    private val _expandedMap = mutableStateMapOf<Int, Boolean>()
    val expandedMap: Map<Int, Boolean> get() = _expandedMap


    private var _isSelectionMode = MutableStateFlow<Boolean>(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    val selectedTasks = mutableStateMapOf<Int, Boolean>()

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(id: Int, title: String, content: TaskRes) {
        viewModelScope.launch {
            repository.updateTask(id, title, content)
        }
    }

    fun findByTaskName(taskName: String) {
        viewModelScope.launch {
            repository.findByTaskName(taskName)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun setUpdateTaskId(id: Int?) {
        _updateTaskId.value = id
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
        selectedTasks[uid] = !(selectedTasks[uid] ?: false)
    }

    fun selectAll() {
        tasks.value.forEach { selectedTasks[it.uid] = true }
    }

    fun unselectAll() {
        selectedTasks.keys.forEach { selectedTasks[it] = false }
    }

    fun deleteSelectedTasks() {
        val toDelete = tasks.value.filter { selectedTasks[it.uid] == true }
        toDelete.forEach { deleteTask(it) }
        toDelete.forEach { selectedTasks.remove(it.uid) }
    }
}
