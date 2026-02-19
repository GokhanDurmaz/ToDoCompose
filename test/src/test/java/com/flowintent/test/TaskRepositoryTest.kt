package com.flowintent.test

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TaskRepositoryTest {

    private lateinit var taskRepository: FakeTaskRepositoryImpl

    @Before
    fun setUp() {
        taskRepository = FakeTaskRepositoryImpl()
    }

    @Test
    fun `getAllTasks should return tasks added to fake repository`() = runTest {
        // Arrange
        val task = createTask(uid = 1, title = "Task 1")
        taskRepository.insertTask(task)

        // Act
        val result = taskRepository.getAllTasks().first()

        // Assert
        assertEquals(1, result.size)
        assertEquals("Task 1", result[0].title)
    }

    @Test
    fun `insertTask should persist task in fake memory`() = runTest {
        // Arrange
        val task = createTask(uid = 99, title = "New Task")

        // Act
        taskRepository.insertTask(task)

        // Assert
        val result = taskRepository.findByTaskName("New Task")
        assertEquals(99, result.uid)
    }

    @Test
    fun `findByTaskName should return correct task from fake storage`() = runTest {
        // Arrange
        val taskName = "Target Task"
        taskRepository.insertTask(createTask(uid = 1, title = taskName))

        // Act
        val result = taskRepository.findByTaskName(taskName)

        // Assert
        assertEquals(taskName, result.title)
    }

    @Test
    fun `deleteTask should remove task from fake storage and return 1`() = runTest {
        // Arrange
        val task = createTask(uid = 10, title = "Delete Me")
        taskRepository.insertTask(task)

        // Act
        val result = taskRepository.deleteTask(task)
        val remainingTasks = taskRepository.getAllTasks().first()

        // Assert
        assertEquals(1, result)
        assertEquals(0, remainingTasks.size)
    }

    private fun createTask(uid: Int, title: String) = Task(
        uid = uid,
        title = title,
        content = TaskRes.TaskContent("Description"),
        taskType = TaskType.OTHER,
        cardColor = 0,
        iconColor = 0,
        textColor = 0,
        dueDate = 0L
    )
}
