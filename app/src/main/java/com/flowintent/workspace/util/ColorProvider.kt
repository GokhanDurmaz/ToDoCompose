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
    return ((alpha * VAL_255).toInt() shl VAL_24) or
            ((red * VAL_255).toInt() shl VAL_16) or
            ((green * VAL_255).toInt() shl VAL_8) or
            (blue * VAL_255).toInt()
}
