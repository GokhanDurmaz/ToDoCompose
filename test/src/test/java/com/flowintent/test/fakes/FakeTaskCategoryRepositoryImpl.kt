/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.fakes

import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.db.repository.TaskCategoryRepository
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

class FakeTaskCategoryRepositoryImpl(
    val localTaskDataProvider: LocalTaskDataProvider
): TaskCategoryRepository {
    override fun getAllLocalCategories(): Flow<Resource<List<TaskCategory>>> = localTaskDataProvider.getAllCategories()
}
