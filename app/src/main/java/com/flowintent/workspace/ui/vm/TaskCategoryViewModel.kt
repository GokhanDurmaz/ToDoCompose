package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.repository.TaskCategoryRepository
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TaskCategoryViewModel @Inject constructor(
    taskCategoryRepository: TaskCategoryRepository
): ViewModel() {
    val allCategories: StateFlow<Resource<List<TaskCategory>>> = taskCategoryRepository.getAllLocalCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading
        )
}
