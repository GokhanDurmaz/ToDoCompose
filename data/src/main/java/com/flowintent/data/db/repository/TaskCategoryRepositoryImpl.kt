package com.flowintent.data.db.repository

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.source.ILocalTaskDataProvider
import com.flowintent.core.db.source.ITaskCategoryRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TaskCategoryRepositoryImpl @Inject constructor(
    val localTaskDataProvider: ILocalTaskDataProvider
): ITaskCategoryRepository {
    override fun getAllLocalCategories(): List<TaskCategory> = runBlocking { localTaskDataProvider.getAllCategories() }
}
