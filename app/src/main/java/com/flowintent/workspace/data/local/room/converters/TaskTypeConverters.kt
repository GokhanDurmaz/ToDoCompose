package com.flowintent.workspace.data.local.room.converters

import androidx.room.TypeConverter
import com.flowintent.workspace.data.TaskRes
import com.flowintent.workspace.data.TaskType
import com.flowintent.workspace.util.TaskTypeConverterJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

@Serializable
data class TaskResWrapper(
    val type: String,
    val content: String? = null,
    val id: Int? = null
)

class TaskTypeConverters {
    @TypeConverter
    fun fromTaskType(taskType: TaskType?): String? {
        return taskType?.name
    }

    @TypeConverter
    fun toTaskType(name: String?): TaskType? {
        return name?.let { TaskType.valueOf(it) }
    }

    @TypeConverter
    fun fromTaskRes(taskRes: TaskRes?): String? {
        if (taskRes == null) {
            return null
        }
        val wrapper = when (taskRes) {
            is TaskRes.TaskContent -> TaskResWrapper(
                type = "TaskContent",
                content = taskRes.content
            )
            is TaskRes.TaskContentRes -> TaskResWrapper(
                type = "TaskContentRes",
                id = taskRes.id
            )
        }
        return TaskTypeConverterJson.encodeToString(wrapper)
    }

    @TypeConverter
    fun toTaskRes(jsonString: String?): TaskRes? {
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        val wrapper = TaskTypeConverterJson.decodeFromString<TaskResWrapper>(jsonString)
        return when(wrapper.type) {
            "TaskContent" -> TaskRes.TaskContent(content = wrapper.content ?: "")
            "TaskContentRes" -> TaskRes.TaskContentRes(id = wrapper.id ?: 0)
            else -> null
        }
    }
}
