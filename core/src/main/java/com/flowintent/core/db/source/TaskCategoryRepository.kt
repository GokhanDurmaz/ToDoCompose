package com.flowintent.core.db.source

import com.flowintent.core.db.TaskCategory

interface TaskCategoryRepository {
    fun getAllLocalCategories(): List<TaskCategory>
}
