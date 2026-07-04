package com.flowintent.workspace.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.flowintent.uikit.util.COLOR_0XFF003366
import com.flowintent.uikit.util.COLOR_0XFF74C3F3
import com.flowintent.uikit.util.VAL_0_0
import com.flowintent.uikit.util.VAL_0_1
import com.flowintent.uikit.util.VAL_0_2
import com.flowintent.uikit.util.VAL_0_3
import com.flowintent.uikit.util.VAL_0_4
import com.flowintent.uikit.util.VAL_0_5
import com.flowintent.uikit.util.VAL_1_0
import com.flowintent.uikit.util.VAL_22
import com.flowintent.uikit.util.VAL_23
import com.flowintent.uikit.util.VAL_25
import com.flowintent.uikit.util.VAL_28
import com.flowintent.uikit.util.VAL_30
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_35
import com.flowintent.uikit.util.VAL_40
import com.flowintent.uikit.util.VAL_45
import com.flowintent.uikit.util.VAL_5
import com.flowintent.uikit.util.VAL_52
import com.flowintent.uikit.util.VAL_55
import com.flowintent.uikit.util.VAL_60
import com.flowintent.uikit.util.VAL_7
import com.flowintent.uikit.util.VAL_72
import com.flowintent.uikit.util.VAL_75

@Composable
fun CustomSplashScreen() {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(108.dp)) {
            val progress = animatable.value

            drawCircle(
                color = Color(COLOR_0XFF003366),
                radius = size.minDimension / 2.5f,
                style = Stroke(width = 2.5.dp.toPx())
            )

            fun drawAnimatedLine(start: Float, y: Float, delayStart: Float, lineLength: Float) {
                val lineProgress = ((progress - delayStart) * (VAL_1_0 / VAL_0_3)).coerceIn(VAL_0_0, VAL_1_0)
                if (lineProgress > VAL_0_0) {
                    drawLine(
                        color = Color(COLOR_0XFF74C3F3),
                        start = androidx.compose.ui.geometry.Offset(start, y),
                        end = androidx.compose.ui.geometry.Offset(start + (lineLength * lineProgress), y),
                        strokeWidth = VAL_5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            drawAnimatedLine(VAL_35.dp.toPx(), VAL_35.dp.toPx(), VAL_0_0, VAL_25.dp.toPx()) // Line 1
            drawAnimatedLine(VAL_32.dp.toPx(), VAL_45.dp.toPx(), VAL_0_1, VAL_23.dp.toPx()) // Line 2
            drawAnimatedLine(VAL_28.dp.toPx(), VAL_55.dp.toPx(), VAL_0_2, VAL_22.dp.toPx()) // Line 3

            val checkProgress = ((progress - VAL_0_4) * (VAL_1_0 / VAL_0_5)).coerceIn(VAL_0_0, VAL_1_0)
            if (checkProgress > VAL_0_0) {
                val path = Path().apply {
                    moveTo(VAL_40.dp.toPx(), VAL_60.dp.toPx())
                    lineTo(VAL_52.dp.toPx(), VAL_72.dp.toPx())
                    lineTo(VAL_75.dp.toPx(), VAL_30.dp.toPx())
                }

                val outPath = Path()
                PathMeasure().apply {
                    setPath(path, forceClosed = false)
                    getSegment(VAL_0_0, length * checkProgress, outPath)
                }

                drawPath(
                    path = outPath,
                    color = Color(COLOR_0XFF003366),
                    style = Stroke(width = VAL_7.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}
