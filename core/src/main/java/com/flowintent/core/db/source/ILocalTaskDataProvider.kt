package com.flowintent.core.db.source

import com.flowintent.core.db.TaskCategory

interface ILocalTaskDataProvider {
    suspend fun getAllCategories(): List<TaskCategory>
}
