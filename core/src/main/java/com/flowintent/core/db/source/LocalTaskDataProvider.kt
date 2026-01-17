package com.flowintent.core.db.source

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocalTaskDataProvider {
    fun getAllCategories(): Flow<Resource<List<TaskCategory>>>
}
