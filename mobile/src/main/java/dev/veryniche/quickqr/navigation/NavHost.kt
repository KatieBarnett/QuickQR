package dev.veryniche.quickqr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import dev.veryniche.quickqr.components.AboutActionIcon
import dev.veryniche.quickqr.components.WelcomeDialog
import dev.veryniche.quickqr.dataStore
import dev.veryniche.quickqr.screens.AboutScreen
import dev.veryniche.quickqr.screens.ExpandedCodeScreen
import dev.veryniche.quickqr.screens.MainScreen
import dev.veryniche.quickqr.util.SetDialogDestinationToEdgeToEdge
import dev.veryniche.quickqr.util.Settings
import kotlinx.coroutines.flow.map

@Composable
fun QuickQRNavHost(
    navController: NavHostController,
    isProPurchased: Boolean,
    onProPurchaseClick: () -> Unit,
) {
    val context = LocalContext.current
    val showWelcome = context.dataStore.data
        .map { preferences -> preferences[Settings.KEY_SHOW_WELCOME] ?: true }
        .collectAsStateWithLifecycle(initialValue = true)
    var showWelcomeThisTime by rememberSaveable { mutableStateOf(true) }

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
                modifier = Modifier
            )
        }
    }

    if (showWelcome.value && showWelcomeThisTime) {
        WelcomeDialog(
            onDismissRequest = { showWelcomeThisTime = false },
            isProPurchased = isProPurchased,
            onProPurchaseClick = onProPurchaseClick,
        )
    }
}
