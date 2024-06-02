package dev.veryniche.quickqr.purchase

import dev.veryniche.quickqr.BuildConfig

fun purchasePro(manager: PurchaseManager,
                onError: (message: Int) -> Unit
) {
    manager.purchase(Products.proVersion, onError)
}

fun isProPurchased(purchasedProducts: List<String>) = purchasedProducts.contains(Products.proVersion) || BuildConfig.DEBUG