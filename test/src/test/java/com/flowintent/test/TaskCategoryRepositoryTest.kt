package com.flowintent.test

import com.flowintent.core.db.TaskCategory
import com.flowintent.core.db.TaskContent
import com.flowintent.core.db.TaskIcon
import com.flowintent.core.db.source.ILocalTaskDataProvider
import com.flowintent.data.db.repository.TaskCategoryRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class TaskCategoryRepositoryTest {

    @Mock
    private lateinit var localTaskDataProvider: ILocalTaskDataProvider

    private lateinit var taskCategoryRepository: TaskCategoryRepositoryImpl

    @Before
    fun setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this)
        // Initialize task category repository impl.
        taskCategoryRepository = TaskCategoryRepositoryImpl(localTaskDataProvider)
    }

    @Test
    fun `getAllLocalCategories should return list of TaskCategory from localTaskDataProvider`() = runBlocking {
        // Arrange: Define the expected categories and mock the data provider behavior
        val expectedCategories = listOf(
            TaskCategory(
                title = "Work",
                content = TaskContent(text = "Work tasks"),
                icon = TaskIcon(type = "svg", name = "briefcase"),
                cardColor = 0xFF6200EE,
                iconColor = 0xFFFFFFFF,
                textColor = 0xFF000000
            ),
            TaskCategory(
                title = "Personal",
                content = TaskContent(text = "Personal tasks"),
                icon = TaskIcon(type = "svg", name = "person"),
                cardColor = 0xFF03DAC5,
                iconColor = 0xFFFFFFFF,
                textColor = 0xFF000000
            ),
            TaskCategory(
                title = "Shopping",
                content = TaskContent(text = "Shopping list"),
                icon = TaskIcon(type = "svg", name = "cart"),
                cardColor = 0xFFFF5722,
                iconColor = 0xFFFFFFFF,
                textColor = 0xFF000000
            )
        )
        `when`(localTaskDataProvider.getAllCategories()).thenReturn(expectedCategories)

        // Act: Call the repository method
        val result = taskCategoryRepository.getAllLocalCategories()

        // Assert: Verify the result matches the expected categories
        assertEquals(expectedCategories, result)
    }

    @Test
    fun `getAllLocalCategories should handle empty list from localTaskDataProvider`() = runBlocking {
        // Arrange: Mock the data provider to return an empty list
        val expectedCategories = emptyList<TaskCategory>()
        `when`(localTaskDataProvider.getAllCategories()).thenReturn(expectedCategories)

        // Act: Call the repository method
        val result = taskCategoryRepository.getAllLocalCategories()

        // Assert: Verify the result is an empty list
        assertEquals(expectedCategories, result)
    }

    @Test
    fun example_test() {
        val expectedResult = 8
        val result = 4.plus(4)
        assertEquals(expectedResult, result)
    }
}
