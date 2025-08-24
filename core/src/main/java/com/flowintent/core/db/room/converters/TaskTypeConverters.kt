package com.flowintent.core.db.room.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.google.gson.Gson
import javax.inject.Inject

data class TaskResWrapper(
    val type: String,
    val content: String? = null,
    val id: Int? = null
)

@ProvidedTypeConverter
class TaskTypeConverters @Inject constructor(
    private val gson: Gson
) {
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
        return gson.toJson(wrapper)
    }

    @TypeConverter
    fun toTaskRes(jsonString: String?): TaskRes? {
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        val wrapper = gson.fromJson(jsonString, TaskResWrapper::class.java)
        return when(wrapper.type) {
            "TaskContent" -> TaskRes.TaskContent(content = wrapper.content ?: "")
            "TaskContentRes" -> TaskRes.TaskContentRes(id = wrapper.id ?: 0)
            else -> null
        }
    }
}
