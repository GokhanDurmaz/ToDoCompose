package com.flowintent.workspace.ui.swipe

import androidx.compose.animation.core.Animatable
import kotlinx.coroutines.CoroutineScope

data class SwipeStateHolder(
    val offsetX: Animatable<Float, *>,
    val maxSwipe: Float,
    val scope: CoroutineScope
)
