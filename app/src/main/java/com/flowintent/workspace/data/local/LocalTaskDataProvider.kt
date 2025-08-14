package com.flowintent.workspace.data.local

import com.flowintent.workspace.R
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.TaskRes

object LocalTaskDataProvider {

    val allTasks = listOf(
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now1",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now2",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now3",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now4",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now5",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now6",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now7",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now8",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now9",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now10",
            creator = "Gesafelstein",
            content = TaskRes.TaskContentRes(R.string.default_song_content)
        ),
    )

    fun get(id: Long): Task? {
        return allTasks.firstOrNull { it.serialNo == id }
    }

    val defaultTask = Task(
        serialNo = -1,
        name = "Unknown",
        content = TaskRes.TaskContentRes(R.string.default_song_content),
    )
}
