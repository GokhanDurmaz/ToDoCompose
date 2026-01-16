package com.flowintent.data.db.repository

import androidx.annotation.VisibleForTesting
import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.db.source.TaskCategoryRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
class TaskCategoryRepositoryImpl @Inject constructor(
    val localTaskDataProvider: LocalTaskDataProvider
): TaskCategoryRepository {
    override fun getAllLocalCategories(): List<TaskCategory> = runBlocking { localTaskDataProvider.getAllCategories() }
}
