package dev.veryniche.quickqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.core.theme.QuickQRTheme

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
            val navController = rememberNavController()

            Text("Hello world")
//            var showWelcomeDialog by remember { mutableStateOf(true) }
//            WelcomeToFlipNavHost(navController = navController)
//            if (showWelcomeDialog) {∂
//                WelcomeDialog(navController) {
//                    showWelcomeDialog = false
//                }
//            }
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        QuickQRThemeMobileApp()
    }
}