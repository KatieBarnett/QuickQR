package dev.veryniche.quickqr.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
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
import dev.veryniche.quickqr.util.SetDialogDestinationToEdgeToEdge

@Composable
fun QuickQRNavHost(
    navController: NavHostController,
    isProPurchased: Boolean,
    onProPurchaseClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
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
                isProPurchased = isProPurchased,
                onProPurchaseClick = onProPurchaseClick,
                snackbarHostState = snackbarHostState,
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
            SetDialogDestinationToEdgeToEdge()
            ExpandedCodeScreen(backStackEntry.arguments?.getInt("id"), Modifier)
        }
        composable(route = About.route,) { backStackEntry ->
            AboutScreen(
                onNavigateBack = { navController.navigateUp() },
                isProPurchased = isProPurchased,
                onProPurchaseClick = onProPurchaseClick,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
            )
        }
    }
}
