package com.flowintent.data.db.repository

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.db.repository.TaskCategoryRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class TaskCategoryRepositoryImpl @Inject constructor(
    val localTaskDataProvider: LocalTaskDataProvider
): TaskCategoryRepository {
    override fun getAllLocalCategories(): List<TaskCategory> = runBlocking { localTaskDataProvider.getAllCategories() }
}
