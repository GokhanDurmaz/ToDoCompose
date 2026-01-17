package com.flowintent.data.db

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.TaskContent
import com.flowintent.core.db.TaskIcon
import com.flowintent.core.db.source.AssetDataSource
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.core.util.Resource
import com.flowintent.data.db.parser.JsonParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

internal class LocalTaskDataProviderImpl @Inject constructor(
    private val jsonDataSource: AssetDataSource,
    private val jsonParser: JsonParser
): LocalTaskDataProvider {
    override fun getAllCategories(): Flow<Resource<List<TaskCategory>>> = flow {
        emit(Resource.Loading)
        try {
            val jsonString = jsonDataSource.readJsonString(JSON_FILE)
            val data = jsonParser.fromJsonList(jsonString, TaskCategory::class.java)
            emit(Resource.Success(data))
        } catch (e: IOException) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    companion object {
        private const val UNKNOWN = "Unknown"
        private const val JSON_FILE = "task_categories.json"
        val defaultTask = TaskCategory(
            title = UNKNOWN,
            content = TaskContent(UNKNOWN),
            icon = TaskIcon(UNKNOWN, UNKNOWN),
            cardColor = -1,
            iconColor = -1,
            textColor = -1
        )
    }
}
