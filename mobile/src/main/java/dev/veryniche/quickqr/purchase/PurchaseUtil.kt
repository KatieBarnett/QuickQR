package dev.veryniche.quickqr.purchase

fun purchasePro(manager: PurchaseManager,
                onError: (message: Int) -> Unit
) {
    manager.purchase(Products.proVersion, onError)
}

fun isProPurchased(purchasedProducts: List<String>) = purchasedProducts.contains(Products.proVersion)