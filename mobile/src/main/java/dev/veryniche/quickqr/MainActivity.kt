package dev.veryniche.quickqr

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.launch
import timber.log.Timber

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
            val coroutineScope = rememberCoroutineScope()
            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
            LaunchedEffect(Unit) {
                purchaseManager.connectToBilling()
            }

            val purchasedProducts by purchaseManager.purchases.collectAsStateWithLifecycle()
            var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }

            QuickQRThemeMobileApp(
                isProPurchased = isProPurchased(purchasedProducts),
                onProPurchaseClick = {
                    purchasePro(purchaseManager, {
                        showPurchaseErrorMessage = it
                    })
                }
            )

            QuickQRTheme {
                showPurchaseErrorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = { showPurchaseErrorMessage = null },
                        title = { Text(stringResource(R.string.pro_purchase_title)) },
                        text = { Text(stringResource(message)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showPurchaseErrorMessage = null
                            }) {
                                Text(stringResource(R.string.purchase_error_dismiss))
                            }
                        }
                    )
                }
            }
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
