/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.repository

import com.flowintent.core.db.model.TaskCategory
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskCategoryRepository {
    fun getAllLocalCategories(): Flow<Resource<List<TaskCategory>>>
}
