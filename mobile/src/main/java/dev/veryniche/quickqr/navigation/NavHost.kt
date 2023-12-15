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
import dev.veryniche.quickqr.screens.ExpandedCodeScreen
import dev.veryniche.quickqr.screens.MainScreen

@Composable
fun QuickQRNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Home.route
    ) {
        composable(Home.route) {
            MainScreen(
                openExpandedQRCode = {
                    navController.navigate(ExpandedCode.getExactRoute(it))
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
