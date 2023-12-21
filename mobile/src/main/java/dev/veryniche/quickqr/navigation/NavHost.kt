package dev.veryniche.quickqr.navigation

import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import dev.veryniche.quickqr.components.AboutActionIcon
import dev.veryniche.quickqr.screens.AboutScreen
import dev.veryniche.quickqr.screens.ExpandedCodeScreen
import dev.veryniche.quickqr.screens.MainScreen

@Composable
fun QuickQRNavHost(
    navController: NavHostController,
    window: Window
) {
    NavHost(
        navController = navController,
        startDestination = Home.route
    ) {
        composable(Home.route) {
            MainScreen(
                openExpandedQRCode = {
                    navController.navigate(ExpandedCode.getExactRoute(it))
                },
                actionIcons = {
                    AboutActionIcon { navController.navigate(About.route) }
                },
                modifier = Modifier
            )
        }
        dialog(
            route = ExpandedCode.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
            dialogProperties = DialogProperties(
                usePlatformDefaultWidth = true,
                decorFitsSystemWindows = false
            )
        ) { backStackEntry ->

            val activityWindow = window
            val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
            val parentView = LocalView.current.parent as View
            SideEffect {
                if (activityWindow != null && dialogWindow != null) {
                    val attributes = WindowManager.LayoutParams()
                    attributes.copyFrom(activityWindow.attributes)
                    attributes.type = dialogWindow.attributes.type
                    dialogWindow.attributes = attributes
                    parentView.layoutParams = FrameLayout.LayoutParams(
                        activityWindow.decorView.width,
                        activityWindow.decorView.height
                    )
                }
            }

            ExpandedCodeScreen(backStackEntry.arguments?.getInt("id"), Modifier)
        }
        composable(route = About.route,) { backStackEntry ->
            AboutScreen(onNavigateBack = { navController.navigateUp() }, Modifier)
        }
    }
}
