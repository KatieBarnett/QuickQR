package dev.veryniche.quickqr.purchase

import android.app.Activity
import android.util.Log
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchasesAsync
import com.google.common.collect.ImmutableList
import dev.veryniche.quickqr.purchase.Products.proVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PurchaseManager(
    private val activity: Activity,
    private val coroutineScope: CoroutineScope
) {
    private val _purchases = MutableStateFlow<List<String>>(emptyList())
    val purchases = _purchases.asStateFlow()

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            coroutineScope.launch {
                processPurchases()
            }
        }

    private var billingClient = BillingClient.newBuilder(activity)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

   fun connectToBilling() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    coroutineScope.launch {
                        processPurchases()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                billingClient.startConnection(this)
            }
        })
    }

    suspend fun processAvailableProducts() {
        val productList = ArrayList<QueryProductDetailsParams.Product>()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Products.proVersion)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
        params.setProductList(productList)

        // leverage queryProductDetails Kotlin extension function
        val productDetailsResult = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params.build())
        }

        // Process the result.
        // Update products list
        _purchases.update {
            val newList = it.toMutableList()
            newList.add(productDetailsResult.productDetailsList?.firstOrNull().toString())
            newList
        }
    }

    suspend fun processPurchases() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        // uses queryPurchasesAsync Kotlin extension function
        val purchasesResult = withContext(Dispatchers.IO) {
            billingClient.queryPurchasesAsync(params.build())
        }

        _purchases.update {
            val newList = it.toMutableList()
            newList.addAll(
                purchasesResult.purchasesList.filter {
                    it.purchaseState == PurchaseState.PURCHASED
                }.map { it.products.firstOrNull().toString() })
            newList
        }
    }

    fun purchase(
        productId: String,
    ) {
        val queryProductDetailsParams =
            QueryProductDetailsParams.newBuilder()
                .setProductList(
                    ImmutableList.of(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(BillingClient.ProductType.INAPP)
                            .build()
                    )
                )
                .build()

        billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val productDetails = productDetailsList.firstOrNull { productDetails ->
                    productDetails.productId == productId
                }
                productDetails?.let {
                    val productDetailsParamsList = listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(it)
                            .build()
                    )
                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()

                    billingClient.launchBillingFlow(activity, billingFlowParams)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
                billingClient.startConnection(object : BillingClientStateListener {
                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            val productDetails = productDetailsList.firstOrNull { productDetails ->
                                productDetails.productId == productId
                            }
                            productDetails?.let {
                                val productDetailsParamsList = listOf(
                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(it)
                                        .build()
                                )
                                val billingFlowParams = BillingFlowParams.newBuilder()
                                    .setProductDetailsParamsList(productDetailsParamsList)
                                    .build()

                                billingClient.launchBillingFlow(activity, billingFlowParams)
                            }
                        }
                    }

                    override fun onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                        // TODO report error
                    }
                })
            }
        }
    }
}
