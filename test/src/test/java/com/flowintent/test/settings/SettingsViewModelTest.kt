package com.flowintent.test.settings

import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.settings.ui.vm.SettingsViewModel
import com.flowintent.test.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var navigationDispatcher: NavigationDispatcher

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SettingsViewModel(navigationDispatcher)
    }

    @Test
    fun `uiState updates when theme changes`() {
        viewModel.onThemeChange("Light")
        assertEquals("Light", viewModel.uiState.value.theme)
    }

    @Test
    fun `uiState updates when DND changes`() {
        viewModel.onDndChange(true)
        assertEquals(true, viewModel.uiState.value.doNotDisturb)
    }
}
