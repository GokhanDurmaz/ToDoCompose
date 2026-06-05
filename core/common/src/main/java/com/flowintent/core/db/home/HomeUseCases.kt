/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.home

import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.db.repository.TaskCategoryRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve home categories.
 */
class GetHomeCategoriesUseCase @Inject constructor(
    private val repository: TaskCategoryRepository
) {
    operator fun invoke(): Flow<Resource<List<TaskCategory>>> = repository.getAllLocalCategories()
}
