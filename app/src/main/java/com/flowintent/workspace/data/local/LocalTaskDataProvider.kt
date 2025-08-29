package com.flowintent.workspace.data.local

import com.flowintent.core.db.source.AssetDataSource
import com.flowintent.workspace.data.parser.JsonParser
import javax.inject.Inject

class LocalTaskDataProvider @Inject constructor(
    private val jsonDataSource: AssetDataSource,
    private val jsonParser: JsonParser
) {
    suspend fun getAllCategories(): List<TaskCategory> {
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

data class TaskCategory(
    val title: String,
    val content: TaskContent,
    val icon: TaskIcon,
    val cardColor: Long,
    val iconColor: Long,
    val textColor: Long
)

data class TaskContent(
    val text: String
)

data class TaskIcon(
    val type: String,
    val name: String
)
