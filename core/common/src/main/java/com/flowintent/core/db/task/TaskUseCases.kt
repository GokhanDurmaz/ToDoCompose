/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.core.db.task

import androidx.paging.PagingData
import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.db.model.TaskType
import com.flowintent.core.db.repository.TaskRepository
import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String? = null, type: TaskType? = null): Flow<PagingData<Task>> {
        return repository.getTasks(query, type)
    }
}

class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.insertTask(task)
}

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Int, title: String, content: TaskRes) = 
        repository.updateTask(id, title, content)
}

class DeleteTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteTaskById(id)
}

class InsertSmartTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(userInput: String): Flow<Resource<Unit>> = repository.insertSmartTask(userInput)
}
