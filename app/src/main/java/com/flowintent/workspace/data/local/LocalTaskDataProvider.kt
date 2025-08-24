package com.flowintent.workspace.data.local

import com.flowintent.core.db.room.Task
import com.flowintent.core.db.room.TaskRes
import com.flowintent.workspace.R

object LocalTaskDataProvider {

    val allTasks = listOf(
        Task(
            name = "Ever Now1",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now2",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now3",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now4",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now5",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now6",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now7",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now8",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now9",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
        Task(
            name = "Ever Now10",
            content = TaskRes.TaskContentRes(R.string.default_task_content)
        ),
    )

    fun get(id: Int): Task? {
        return allTasks.firstOrNull { it.uid == id }
    }

    val defaultTask = Task(
        uid = -1,
        name = "Unknown",
        content = TaskRes.TaskContentRes(R.string.default_task_content),
    )
}
