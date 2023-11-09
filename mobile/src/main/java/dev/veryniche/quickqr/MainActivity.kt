package dev.veryniche.quickqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.screens.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickQRThemeMobileApp()
        }
    }

    @Composable
    fun QuickQRThemeMobileApp() {
        QuickQRTheme {
            MainScreen()
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        QuickQRThemeMobileApp()
    }
}
