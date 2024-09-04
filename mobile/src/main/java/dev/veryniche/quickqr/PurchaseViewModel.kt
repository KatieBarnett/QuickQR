package dev.veryniche.quickqr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.analytics.trackPurchaseClick
import dev.veryniche.quickqr.purchase.Products
import dev.veryniche.quickqr.purchase.PurchaseManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PurchaseViewModel.PurchaseViewModelFactory::class)
class PurchaseViewModel @AssistedInject constructor(
    @Assisted val purchaseManager: PurchaseManager,
) : ViewModel() {

    @AssistedFactory
    interface PurchaseViewModelFactory {
        fun create(purchaseManager: PurchaseManager): PurchaseViewModel
    }

    val isProPurchased = purchaseManager.purchases.map { it.contains(Products.proVersion) || BuildConfig.DEBUG }

    init {
        viewModelScope.launch {
            purchaseManager.connectToBilling()
        }
    }

    fun purchaseProduct(productId: String, onError: (message: Int) -> Unit) {
        viewModelScope.launch {
            trackPurchaseClick(productId)
            purchaseManager.purchase(
                productId = productId,
                onError = onError
            )
        }
    }
}
