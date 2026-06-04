/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.settings

import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.settings.ui.vm.SettingsViewModel
import com.flowintent.test.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var navigationDispatcher: NavigationDispatcher

    @Mock
    private lateinit var repo: EncryptedProtoRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(repo.languageFlow()).thenReturn(flowOf("en"))
        `when`(repo.themeFlow()).thenReturn(flowOf("Dark"))
        viewModel = SettingsViewModel(navigationDispatcher, repo)
    }

    @Test
    fun `uiState updates when theme changes`() {
        viewModel.onThemeChange("Light")
        assertEquals("Light", viewModel.uiState.value.theme)
    }

    @Test
    fun `uiState updates when DND changes`() {
        viewModel.onDndChange(enabled = true)
        assertEquals(true, viewModel.uiState.value.doNotDisturb)
    }
}
