package com.flowintent.core.db.repository

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface TaskCategoryRepository {
    fun getAllLocalCategories(): Flow<Resource<List<TaskCategory>>>
}
