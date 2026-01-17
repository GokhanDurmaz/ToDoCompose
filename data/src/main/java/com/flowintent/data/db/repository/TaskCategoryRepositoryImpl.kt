package com.flowintent.data.db.repository

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.db.repository.TaskCategoryRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TaskCategoryRepositoryImpl @Inject constructor(
    val localTaskDataProvider: LocalTaskDataProvider
): TaskCategoryRepository {
    override fun getAllLocalCategories(): Flow<Resource<List<TaskCategory>>> =
        localTaskDataProvider.getAllCategories()
}
