/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.notification

import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
import com.flowintent.core.notification.TaskNotificationScheduler
import com.flowintent.test.fakes.FakeTaskRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class TaskNotificationTest {

    @Mock
    private lateinit var notificationScheduler: TaskNotificationScheduler

    private lateinit var taskRepository: FakeTaskRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        taskRepository = FakeTaskRepositoryImpl(notificationScheduler)
    }

    @Test
    fun `insertTask should schedule notification`() = runTest {
        val task = createTask(uid = 1, title = "Test Task")
        taskRepository.insertTask(task)

        verify(notificationScheduler).schedule(task)
    }

    @Test
    fun `deleteTask should cancel notification`() = runTest {
        val task = createTask(uid = 1, title = "Delete Task")
        taskRepository.insertTask(task)
        
        taskRepository.deleteTask(task)

        verify(notificationScheduler).cancel(task)
    }

    @Test
    fun `updateTask should reschedule notification`() = runTest {
        val task = createTask(uid = 1, title = "Original Task")
        taskRepository.insertTask(task)

        val updatedTask = task.copy(title = "Updated Task")
        taskRepository.updateTask(updatedTask)

        verify(notificationScheduler).schedule(updatedTask)
    }

    private fun createTask(uid: Int, title: String) = Task(
        uid = uid,
        title = title,
        content = TaskRes.TaskContent("Content"),
        dueDate = System.currentTimeMillis() + 3600000 // 1 hour later
    )
}
