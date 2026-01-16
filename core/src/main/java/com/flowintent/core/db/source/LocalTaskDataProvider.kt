package com.flowintent.core.db.source

import com.flowintent.core.db.TaskCategory

interface LocalTaskDataProvider {
    suspend fun getAllCategories(): List<TaskCategory>
}
