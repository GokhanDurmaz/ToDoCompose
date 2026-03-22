package com.flowintent.test

import com.flowintent.core.db.model.Task
import com.flowintent.core.db.model.TaskRes
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
        val task = createTask(uid = 1, title = "Task 1")
        taskRepository.insertTask(task)

        val result = taskRepository.getAllTasksRaw().first()

        assertEquals(1, result.size)
        assertEquals("Task 1", result[0].title)
    }

    @Test
    fun `insertTask should persist task in fake memory`() = runTest {
        val task = createTask(uid = 99, title = "New Task")
        taskRepository.insertTask(task)

        val result = taskRepository.getAllTasksRaw().first()
        val found = result.find { it.title == "New Task" }

        assertEquals(99, found?.uid)
    }

    @Test
    fun `deleteTask should remove task from fake storage and return 1`() = runTest {
        val task = createTask(uid = 10, title = "Delete Me")
        taskRepository.insertTask(task)

        val deleteResult = taskRepository.deleteTask(task)
        val remainingTasks = taskRepository.getAllTasksRaw().first()

        assertEquals(1, deleteResult)
        assertEquals(0, remainingTasks.size)
    }

    @Test
    fun `deleteTaskById should remove correct task`() = runTest {
        taskRepository.insertTask(createTask(uid = 5, title = "Target"))

        val result = taskRepository.deleteTaskById(5)
        val remaining = taskRepository.getAllTasksRaw().first()

        assertEquals(1, result)
        assertEquals(0, remaining.size)
    }

    private fun createTask(uid: Int, title: String) = Task(
        uid = uid,
        title = title,
        content = TaskRes.TaskContent("Description"),
        dueDate = 0L
    )
}
