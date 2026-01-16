package com.flowintent.core.db.repository

import com.flowintent.core.db.TaskCategory

interface TaskCategoryRepository {
    fun getAllLocalCategories(): List<TaskCategory>
}
