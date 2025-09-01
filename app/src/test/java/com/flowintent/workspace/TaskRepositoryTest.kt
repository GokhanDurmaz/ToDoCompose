package com.flowintent.workspace

import com.flowintent.core.db.Task
import com.flowintent.core.db.TaskRes
import com.flowintent.core.db.TaskType
import com.flowintent.core.db.room.dao.ToDoDao
import com.flowintent.workspace.data.local.repository.TaskRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class TaskRepositoryTest {

    @Mock
    private lateinit var toDoDao: ToDoDao

    private lateinit var taskRepository: TaskRepository

    @Before
    fun setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this)
        // Initialize the repository with the mocked DAO
        taskRepository = TaskRepository(toDoDao)
    }

    @Test
    fun `getAllTasks should return Flow of tasks from toDoDao`() = runTest {
        // Arrange: Define the expected tasks and mock the DAO behavior
        val expectedTasks = listOf(
            Task(
                uid = 1,
                title = "Task 1",
                content = TaskRes.TaskContent("Do work"),
                taskType = TaskType.LOCAL_TASKS,
                cardColor = 0xFF6200EE.toInt(),
                iconColor = 0xFFFFFFFF.toInt(),
                textColor = 0xFF000000.toInt()
            ),
            Task(
                uid = 2,
                title = "Task 2",
                content = TaskRes.TaskContent("Buy groceries"),
                taskType = TaskType.LOCAL_TASKS,
                cardColor = 0xFF03DAC5.toInt(),
                iconColor = 0xFFFFFFFF.toInt(),
                textColor = 0xFF000000.toInt()
            )
        )
        `when`(toDoDao.getAllTasks()).thenReturn(flowOf(expectedTasks))

        // Act: Call the repository method and collect the first emission
        val result = taskRepository.getAllTasks().first()

        // Assert: Verify the result matches the expected tasks
        assertEquals(expectedTasks, result)
    }

    @Test
    fun `insertTask should call toDoDao insertTask with the provided task`() = runTest {
        // Arrange: Create a sample task
        val task = Task(
            uid = 0, // Auto-generated
            title = "New Task",
            content = TaskRes.TaskContent("Complete project"),
            taskType = TaskType.LOCAL_TASKS,
            cardColor = 0xFF6200EE.toInt(),
            iconColor = 0xFFFFFFFF.toInt(),
            textColor = 0xFF000000.toInt()
        )

        // Act: Call the repository method
        taskRepository.insertTask(task)

        // Assert: Verify the DAO's insertTask was called with the correct task
        verify(toDoDao).insertTask(task)
    }

    @Test
    fun `findByTaskName should call toDoDao findByTaskName with the provided taskName`() = runTest {
        // Arrange: Define the task name and mock the DAO behavior
        val taskName = "Task 1"
        val expectedTask = Task(
            uid = 1,
            title = taskName,
            content = TaskRes.TaskContent("Do work"),
            taskType = TaskType.LOCAL_TASKS,
            cardColor = 0xFF6200EE.toInt(),
            iconColor = 0xFFFFFFFF.toInt(),
            textColor = 0xFF000000.toInt()
        )
        `when`(toDoDao.findByTaskName(taskName)).thenReturn(expectedTask)

        // Act: Call the repository method
        taskRepository.findByTaskName(taskName)

        // Assert: Verify the DAO's findByTaskName was called with the correct taskName
        verify(toDoDao).findByTaskName(taskName)
    }

    @Test
    fun `deleteTask should call toDoDao delete and return the number of rows affected`() = runTest {
        // Arrange: Create a sample task and mock the DAO behavior
        val task = Task(
            uid = 1,
            title = "Task to delete",
            content = TaskRes.TaskContent("Delete this"),
            taskType = TaskType.LOCAL_TASKS,
            cardColor = 0xFF6200EE.toInt(),
            iconColor = 0xFFFFFFFF.toInt(),
            textColor = 0xFF000000.toInt()
        )
        val expectedRowsAffected = 1
        `when`(toDoDao.delete(task)).thenReturn(expectedRowsAffected)

        // Act: Call the repository method
        val result = taskRepository.deleteTask(task)

        // Assert: Verify the DAO's delete was called and the result matches
        verify(toDoDao).delete(task)
        assertEquals(expectedRowsAffected, result)
    }
}
