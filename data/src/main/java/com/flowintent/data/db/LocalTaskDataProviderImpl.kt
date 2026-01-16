package com.flowintent.data.db

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.TaskContent
import com.flowintent.core.db.TaskIcon
import com.flowintent.core.db.source.AssetDataSource
import com.flowintent.core.db.source.LocalTaskDataProvider
import com.flowintent.data.db.parser.JsonParser
import javax.inject.Inject

internal class LocalTaskDataProviderImpl @Inject constructor(
    private val jsonDataSource: AssetDataSource,
    private val jsonParser: JsonParser
): LocalTaskDataProvider {
    override suspend fun getAllCategories(): List<TaskCategory> {
        val jsonString = jsonDataSource.readJsonString(JSON_FILE)
        return jsonParser.fromJsonList(jsonString, TaskCategory::class.java)
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
