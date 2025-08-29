package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.workspace.data.local.repository.TaskCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskCategoryViewModel @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepository
): ViewModel() {
    fun getAllCategories() = taskCategoryRepository.getAllLocalCategories()
}
