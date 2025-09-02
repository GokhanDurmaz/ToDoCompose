package com.flowintent.workspace.ui.vm

import androidx.lifecycle.ViewModel
import com.flowintent.data.db.repository.TaskCategoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskCategoryViewModel @Inject constructor(
    private val taskCategoryRepository: TaskCategoryRepositoryImpl
): ViewModel() {
    fun getAllCategories() = taskCategoryRepository.getAllLocalCategories()
}
