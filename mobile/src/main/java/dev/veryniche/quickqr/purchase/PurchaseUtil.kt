package dev.veryniche.quickqr.purchase

fun purchasePro(manager: PurchaseManager,
                onError: (message: Int) -> Unit
) {
    manager.purchase(Products.proVersion, onError)
}

fun isProPurchased(manager: List<String>) = manager.contains(Products.proVersion)