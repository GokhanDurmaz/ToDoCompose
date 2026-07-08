 /**
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.workspace.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flowintent.auth.ui.vm.AuthViewModel
import com.flowintent.core.util.AppEventTracker
import com.flowintent.navigation.FeatureApi
import com.flowintent.navigation.NavigationDispatcher
import com.flowintent.settings.ui.vm.SettingsViewModel
import com.flowintent.uikit.theme.ToDoTheme
import com.flowintent.uikit.theme.md_theme_light_primary
import com.flowintent.workspace.nav.ToDoNavigationBar
import com.flowintent.workspace.ui.splash.CustomSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

 @AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationDispatcher: NavigationDispatcher
    @Inject
    lateinit var eventTracker: AppEventTracker
    @Inject
    @JvmSuppressWildcards
    lateinit var featureApi: Set<FeatureApi>
    private val authViewModel: AuthViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            val isSystemDark = isSystemInDarkTheme()
            val isDarkTheme = when (settingsUiState.theme) {
                "Dark" -> true
                "Light" -> false
                else -> isSystemDark
            }

            LaunchedEffect(isDarkTheme) {
                val windowInsetsController =
                    WindowInsetsControllerCompat(window, window.decorView)
                windowInsetsController.isAppearanceLightStatusBars = !isDarkTheme
                windowInsetsController.isAppearanceLightNavigationBars = !isDarkTheme
            }

            HomeScreen(
                isDarkTheme = isDarkTheme,
                authViewModel = authViewModel,
                navigationDispatcher = navigationDispatcher,
                featureApi = featureApi,
                eventTracker = eventTracker
            )
        }
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
fun HomeScreen(
    isDarkTheme: Boolean,
    authViewModel: AuthViewModel,
    navigationDispatcher: NavigationDispatcher,
    featureApi: Set<FeatureApi>,
    eventTracker: AppEventTracker
) {
    ToDoTheme(darkTheme = isDarkTheme) {
        val isReady by authViewModel.isReady.collectAsStateWithLifecycle()

        Crossfade(targetState = isReady, label = "splash_transition") { ready ->
            if (ready) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = md_theme_light_primary
                ) {
                    ToDoNavigationBar(
                        authViewModel = authViewModel,
                        navigationDispatcher = navigationDispatcher,
                        featureApis = featureApi,
                        eventTracker = eventTracker
                    )
                }
            } else {
                CustomSplashScreen()
            }
        }
    }
}
