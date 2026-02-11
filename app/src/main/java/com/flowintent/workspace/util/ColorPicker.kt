package com.flowintent.workspace.util

import androidx.compose.ui.graphics.Color

class ColorPicker(initialColors: List<Color>) {
    private val colors = initialColors.shuffled().toMutableList()
    private var index = 0

    fun next(): Color {
        if (index >= colors.size) index = 0
        return colors[index++]
    }
}
