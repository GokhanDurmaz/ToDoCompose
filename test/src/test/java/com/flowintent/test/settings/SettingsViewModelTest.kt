/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.settings

import com.flowintent.core.db.repository.EncryptedProtoRepository
import com.flowintent.core.db.settings.GetLanguageUseCase
import com.flowintent.core.db.settings.GetProtoThemeUseCase
import com.flowintent.core.db.settings.UpdateLanguageUseCase
import com.flowintent.core.db.settings.UpdateProtoThemeUseCase
import com.flowintent.core.util.AppEventTracker
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

    @Mock
    private lateinit var eventTracker: AppEventTracker

    private lateinit var getLanguageUseCase: GetLanguageUseCase
    private lateinit var updateLanguageUseCase: UpdateLanguageUseCase
    private lateinit var getProtoThemeUseCase: GetProtoThemeUseCase
    private lateinit var updateProtoThemeUseCase: UpdateProtoThemeUseCase

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        
        getLanguageUseCase = GetLanguageUseCase(repo)
        updateLanguageUseCase = UpdateLanguageUseCase(repo)
        getProtoThemeUseCase = GetProtoThemeUseCase(repo)
        updateProtoThemeUseCase = UpdateProtoThemeUseCase(repo)

        `when`(repo.languageFlow()).thenReturn(flowOf("en"))
        `when`(repo.themeFlow()).thenReturn(flowOf("Dark"))
        
        viewModel = SettingsViewModel(
            navigationDispatcher,
            getLanguageUseCase,
            updateLanguageUseCase,
            getProtoThemeUseCase,
            updateProtoThemeUseCase,
            eventTracker
        )
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
