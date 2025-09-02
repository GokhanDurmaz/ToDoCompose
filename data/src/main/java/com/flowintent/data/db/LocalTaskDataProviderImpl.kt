package com.flowintent.data.db

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.TaskContent
import com.flowintent.core.db.TaskIcon
import com.flowintent.core.db.source.IAssetDataSource
import com.flowintent.core.db.source.ILocalTaskDataProvider
import com.flowintent.data.db.parser.JsonParser
import javax.inject.Inject

class LocalTaskDataProviderImpl @Inject constructor(
    private val jsonDataSource: IAssetDataSource,
    private val jsonParser: JsonParser
): ILocalTaskDataProvider {
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
