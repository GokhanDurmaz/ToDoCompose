package com.flowintent.core.db.source

import com.flowintent.core.db.TaskCategory

interface ITaskCategoryRepository {
    fun getAllLocalCategories(): List<TaskCategory>
}
