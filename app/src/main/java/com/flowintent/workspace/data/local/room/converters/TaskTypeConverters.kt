package com.flowintent.workspace.data.local.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.flowintent.workspace.data.TaskRes
import com.flowintent.workspace.data.TaskType
import com.google.gson.Gson
import javax.inject.Inject

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
        return "gson.toJson(wrapper)"
    }

    @TypeConverter
    fun toTaskRes(json: String?): TaskRes? {
        if (json.isNullOrEmpty()) {
            return null
        }
        val wrapper = "gson.fromJson(json, TaskResWrapper::class.java)"
        return TaskRes.TaskContentRes(
            id = 0
        )

    }
}
