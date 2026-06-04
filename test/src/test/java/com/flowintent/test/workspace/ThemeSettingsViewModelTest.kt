/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import com.flowintent.core.db.model.AppTheme
import com.flowintent.core.db.settings.GetThemeUseCase
import com.flowintent.core.db.settings.UpdateThemeUseCase
import com.flowintent.test.rules.MainDispatcherRule
import com.flowintent.workspace.ui.vm.ThemeSettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ThemeSettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var getThemeUseCase: GetThemeUseCase

    @Mock
    private lateinit var updateThemeUseCase: UpdateThemeUseCase

    private lateinit var viewModel: ThemeSettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(getThemeUseCase()).thenReturn(flowOf(AppTheme("default", "light")))
    }

    @Test
    fun `currentTheme updates when getThemeUseCase emits`() = runTest {
        val newTheme = AppTheme("dark", "dark")
        whenever(getThemeUseCase()).thenReturn(flowOf(newTheme))

        viewModel = ThemeSettingsViewModel(getThemeUseCase, updateThemeUseCase)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.currentTheme.collect {}
        }

        assertEquals(newTheme, viewModel.currentTheme.value)
    }

    @Test
    fun `setTheme calls updateThemeUseCase`() = runTest {
        viewModel = ThemeSettingsViewModel(getThemeUseCase, updateThemeUseCase)
        val theme = AppTheme("dark", "dark")
        viewModel.setTheme(theme)
        verify(updateThemeUseCase).invoke(theme)
    }
}
