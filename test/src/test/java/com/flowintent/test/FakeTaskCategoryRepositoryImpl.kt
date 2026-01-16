package com.flowintent.test

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.db.source.TaskCategoryRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FakeTaskCategoryRepositoryImpl @Inject constructor(
    val localTaskDataProvider: LocalTaskDataProvider
): TaskCategoryRepository {
    override fun getAllLocalCategories(): List<TaskCategory> = runBlocking { localTaskDataProvider.getAllCategories() }
}
