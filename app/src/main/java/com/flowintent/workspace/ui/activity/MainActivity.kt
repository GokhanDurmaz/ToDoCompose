package com.flowintent.workspace.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.flowintent.workspace.nav.ToDoNavigationBar
import com.flowintent.workspace.theme.ToDoTheme
import com.flowintent.workspace.theme.md_theme_light_primary
import com.flowintent.workspace.ui.vm.AuthViewModel
import com.flowintent.workspace.util.COLOR_0XFF003366
import com.flowintent.workspace.util.COLOR_0XFF74C3F3
import com.flowintent.workspace.util.VAL_0_0
import com.flowintent.workspace.util.VAL_0_1
import com.flowintent.workspace.util.VAL_0_2
import com.flowintent.workspace.util.VAL_0_3
import com.flowintent.workspace.util.VAL_0_4
import com.flowintent.workspace.util.VAL_0_5
import com.flowintent.workspace.util.VAL_1_0
import com.flowintent.workspace.util.VAL_22
import com.flowintent.workspace.util.VAL_23
import com.flowintent.workspace.util.VAL_25
import com.flowintent.workspace.util.VAL_28
import com.flowintent.workspace.util.VAL_30
import com.flowintent.workspace.util.VAL_32
import com.flowintent.workspace.util.VAL_35
import com.flowintent.workspace.util.VAL_40
import com.flowintent.workspace.util.VAL_45
import com.flowintent.workspace.util.VAL_5
import com.flowintent.workspace.util.VAL_52
import com.flowintent.workspace.util.VAL_55
import com.flowintent.workspace.util.VAL_60
import com.flowintent.workspace.util.VAL_7
import com.flowintent.workspace.util.VAL_72
import com.flowintent.workspace.util.VAL_75
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(isDarkTheme) {
                val windowInsetsController =
                    WindowInsetsControllerCompat(window, window.decorView)
                windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }
            HomeScreen(viewModel = authViewModel)
        }
        Log.i(TAG, "onCreate")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    companion object {
        private val TAG: String = MainActivity::class.simpleName.toString()
    }
}

@Composable
fun HomeScreen(viewModel: AuthViewModel) {
    ToDoTheme {
        val isReady by viewModel.isReady.collectAsState()

        Crossfade(targetState = isReady, label = "splash_transition") { ready ->
            if (ready) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = md_theme_light_primary
                ) {
                    ToDoNavigationBar(WindowWidthSizeClass.Compact, authViewModel = viewModel)
                }
            } else {
                CustomSplashScreen()
            }
        }
    }
}

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
            .background(Color.White),
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
                    setPath(path, false)
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
