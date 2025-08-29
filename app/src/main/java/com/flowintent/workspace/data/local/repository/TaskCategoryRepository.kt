package com.flowintent.workspace.data.local.repository

import com.flowintent.workspace.data.local.LocalTaskDataProvider
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TaskCategoryRepository @Inject constructor(
    val localTaskDataProvider: LocalTaskDataProvider
) {

    fun getAllLocalCategories() = runBlocking { localTaskDataProvider.getAllCategories() }
}