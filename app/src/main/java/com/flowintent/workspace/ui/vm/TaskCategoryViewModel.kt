package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.core.db.source.ITaskCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskCategoryViewModel @Inject constructor(
    private val taskCategoryRepository: ITaskCategoryRepository
): ViewModel() {
    fun getAllCategories() = taskCategoryRepository.getAllLocalCategories()
}
