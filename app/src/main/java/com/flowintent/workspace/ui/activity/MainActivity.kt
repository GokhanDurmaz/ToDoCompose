package com.flowintent.workspace.ui.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.ToDoNavigationBar
import com.flowintent.workspace.theme.ToDoTheme
import com.flowintent.workspace.theme.md_theme_light_primary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_App_Splash)
        }

        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            LaunchedEffect(isDarkTheme) {
                val windowInsetsController =
                    WindowInsetsControllerCompat(window, window.decorView)
                windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }
            HomeScreen()
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

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    ToDoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = md_theme_light_primary
        ) {
            ToDoNavigationBar(WindowWidthSizeClass.Compact)
        }
    }
}
