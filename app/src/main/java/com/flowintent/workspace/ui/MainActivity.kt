package com.flowintent.workspace.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flowintent.workspace.theme.MainTheme


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    MainTheme {
        Surface {
            MainScreen(
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }
}
