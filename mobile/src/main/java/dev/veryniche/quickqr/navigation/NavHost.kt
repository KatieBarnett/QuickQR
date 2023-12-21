package dev.veryniche.quickqr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.google.firebase.analytics.FirebaseAnalytics
import dev.veryniche.quickqr.components.AboutActionIcon
import dev.veryniche.quickqr.screens.ExpandedCodeScreen
import dev.veryniche.quickqr.screens.MainScreen
import dev.veryniche.quickqr.util.Analytics
import dev.veryniche.quickqr.util.TrackedScreen
import dev.veryniche.quickqr.util.trackMainScreenView
import dev.veryniche.quickqr.util.trackScreenView

@Composable
fun QuickQRNavHost(
    navController: NavHostController,
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
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = true
            )
        ) { backStackEntry ->
            ExpandedCodeScreen(backStackEntry.arguments?.getInt("id"), Modifier)
        }
    }
}
