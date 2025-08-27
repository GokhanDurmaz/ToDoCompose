package com.flowintent.workspace.ui.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.ToDoNavigationBar
import com.flowintent.workspace.theme.ToDoTheme
import com.flowintent.workspace.theme.md_theme_light_primary
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setUpEdgeToEdge()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_App_Splash)
        }

        setContent {
            HomeScreen()
        }
    }

    fun ComponentActivity.setUpEdgeToEdge() {
        val window = window
        val view = findViewById<View>(android.R.id.content)
        val resources = view.resources
        val theme = theme

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val transparent = ResourcesCompat.getColor(resources, android.R.color.transparent, theme)
        val isDarkMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES

        when {
            Build.VERSION.SDK_INT >= 29 -> {
                window.statusBarColor = transparent
                window.navigationBarColor = transparent

                WindowInsetsControllerCompat(window, view).apply {
                    isAppearanceLightStatusBars = !isDarkMode
                    isAppearanceLightNavigationBars = !isDarkMode
                }
            }
            Build.VERSION.SDK_INT >= 26 -> {
                window.statusBarColor = transparent
                val scrim = ResourcesCompat.getColor(
                    resources,
                    R.color.navigation_bar_scrim_light, // kendi rengin
                    theme
                )
                window.navigationBarColor = scrim

                WindowInsetsControllerCompat(window, view).apply {
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
            Build.VERSION.SDK_INT >= 23 -> {
                window.statusBarColor = transparent

                WindowInsetsControllerCompat(window, view).apply {
                    isAppearanceLightStatusBars = true
                }

                @Suppress("DEPRECATION")
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            Build.VERSION.SDK_INT >= 21 -> {
                @Suppress("DEPRECATION")
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                @Suppress("DEPRECATION")
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            }
            else -> Unit
        }
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
