/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.workspace

import com.flowintent.core.db.settings.GetAppVersionUseCase
import com.flowintent.core.db.settings.OpenPrivacyPolicyUseCase
import com.flowintent.core.db.settings.OpenTermsOfServiceUseCase
import com.flowintent.workspace.ui.vm.AboutSettingsViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AboutSettingsViewModelTest {

    @Mock
    private lateinit var getAppVersionUseCase: GetAppVersionUseCase
    @Mock
    private lateinit var openPrivacyPolicyUseCase: OpenPrivacyPolicyUseCase
    @Mock
    private lateinit var openTermsOfServiceUseCase: OpenTermsOfServiceUseCase

    private lateinit var viewModel: AboutSettingsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AboutSettingsViewModel(getAppVersionUseCase, openPrivacyPolicyUseCase, openTermsOfServiceUseCase)
    }

    @Test
    fun `getAppVersion returns version from use case`() = runTest {
        whenever(getAppVersionUseCase()).thenReturn("1.2.3")
        val version = viewModel.getAppVersion()
        assertEquals("1.2.3", version)
    }

    @Test
    fun `openPrivacyPolicy calls use case`() = runTest {
        viewModel.openPrivacyPolicy()
        verify(openPrivacyPolicyUseCase).invoke()
    }

    @Test
    fun `openTermsOfService calls use case`() = runTest {
        viewModel.openTermsOfService()
        verify(openTermsOfServiceUseCase).invoke()
    }
}
