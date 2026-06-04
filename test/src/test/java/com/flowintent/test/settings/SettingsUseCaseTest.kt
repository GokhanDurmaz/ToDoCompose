/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.settings

import com.flowintent.core.db.model.AppTheme
import com.flowintent.core.db.repository.SettingsRepository
import com.flowintent.core.db.settings.GetThemeUseCase
import com.flowintent.core.db.settings.UpdateThemeUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SettingsUseCaseTest {

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var getThemeUseCase: GetThemeUseCase
    private lateinit var updateThemeUseCase: UpdateThemeUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getThemeUseCase = GetThemeUseCase(settingsRepository)
        updateThemeUseCase = UpdateThemeUseCase(settingsRepository)
    }

    @Test
    fun `GetThemeUseCase returns theme from repository`() = runTest {
        val expectedTheme = AppTheme("Dark", "Black")
        whenever(settingsRepository.getTheme()).thenReturn(flowOf(expectedTheme))

        getThemeUseCase().collect { theme ->
            assertEquals(expectedTheme, theme)
        }
    }

    @Test
    fun `UpdateThemeUseCase calls updateTheme in repository`() = runTest {
        val newTheme = AppTheme("Light", "White")

        updateThemeUseCase(newTheme)

        verify(settingsRepository).updateTheme(newTheme)
    }
}
