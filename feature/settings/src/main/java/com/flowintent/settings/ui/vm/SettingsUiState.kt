package com.flowintent.settings.ui.vm

data class SettingsUiState(
    val theme: String = "Dark",
    val doNotDisturb: Boolean = false,
    val dndIntensity: Float = 0.5f,
    val currentLocale: String = "en"
)
