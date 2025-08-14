package com.flowintent.workspace.data

data class Task(
    val serialNo: Long,
    val name: String = "",
    val creator: String = "",
    var content: TaskRes,
    var tasktype: TaskType = TaskType.LOCAL_TASKS
)
