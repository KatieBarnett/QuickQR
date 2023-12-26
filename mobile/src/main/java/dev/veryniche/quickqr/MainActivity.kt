package dev.veryniche.quickqr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.navigation.QuickQRNavHost
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.purchase.isProPurchased
import dev.veryniche.quickqr.purchase.purchasePro
import dev.veryniche.quickqr.util.Settings

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Settings.DATA_STORE_KEY)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
        setContent {
            val purchaseManager = remember { PurchaseManager(this) }
            LaunchedEffect(Unit) {
                purchaseManager.billingSetup()
                purchaseManager.checkProducts()
            }

            val purchasedProducts by purchaseManager.purchases.collectAsStateWithLifecycle()

            QuickQRThemeMobileApp(
                isProPurchased = isProPurchased(purchasedProducts),
                onProPurchaseClick = { purchasePro(purchaseManager) }
            )
        }
    }

    @Composable
    fun QuickQRThemeMobileApp(isProPurchased: Boolean, onProPurchaseClick: () -> Unit) {
        QuickQRTheme {
            val navController = rememberNavController()
            QuickQRNavHost(
                navController = navController,
                isProPurchased = isProPurchased,
                onProPurchaseClick = onProPurchaseClick
            )
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        QuickQRThemeMobileApp(true, {})
    }
}
