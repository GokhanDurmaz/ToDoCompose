package com.flowintent.workspace.ui.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.flowintent.workspace.R
import com.flowintent.workspace.nav.ToDoNavigationBar
import com.flowintent.workspace.theme.ToDoTheme
import com.flowintent.workspace.theme.md_theme_light_primary
import com.flowintent.workspace.ui.vm.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_App_Splash)
        }

        setContent {
            HomeScreen(viewModel)
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: TaskViewModel
) {
    ToDoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = md_theme_light_primary
        ) {
            ToDoNavigationBar(
                modifier = Modifier,
                viewModel = viewModel,
                WindowWidthSizeClass.Compact
            )
        }
    }
}
