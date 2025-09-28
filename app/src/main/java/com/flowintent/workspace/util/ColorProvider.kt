package com.flowintent.workspace.util

import androidx.compose.ui.graphics.Color

object ColorProvider {
    private val baseColors = listOf(
        Color(0xFF2B17DA),
        Color(0xFFE91E63),
        Color(0xFF4CAF50),
        Color(0xFFFF9800),
        Color(0xFF009688)
    )

    fun getShuffledColors(): List<Color> = baseColors.shuffled()
}

fun Color.toArgbCompat(): Int {
    return ((alpha * 255).toInt() shl 24) or
            ((red * 255).toInt() shl 16) or
            ((green * 255).toInt() shl 8) or
            (blue * 255).toInt()
}
