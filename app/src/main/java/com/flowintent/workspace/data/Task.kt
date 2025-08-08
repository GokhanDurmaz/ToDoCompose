package com.flowintent.workspace.data

data class Task(
    val serialNo: Long,
    val name: String = "",
    val creator: String = "",
    var content: SongRes,
    var song: TaskType = TaskType.LOCAL_SONGS
)
