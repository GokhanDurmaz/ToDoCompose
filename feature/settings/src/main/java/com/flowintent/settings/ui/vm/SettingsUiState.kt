 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.settings.ui.vm

data class SettingsUiState(
    val theme: String = "Light",
    val doNotDisturb: Boolean = false,
    val dndIntensity: Float = 0.5f,
    val currentLocale: String = "en"
)
