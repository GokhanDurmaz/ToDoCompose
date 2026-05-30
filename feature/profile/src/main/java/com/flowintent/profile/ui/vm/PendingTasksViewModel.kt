package com.flowintent.profile.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PendingTasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks: Flow<PagingData<Task>> = repository.getTasks(null, null)
        .cachedIn(viewModelScope)
}
