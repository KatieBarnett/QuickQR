package dev.veryniche.quickqr.purchase

fun purchasePro(manager: PurchaseManager) {
    manager.purchase(Products.proVersion)
}

fun isProPurchased(manager: List<String>) = manager.contains(Products.proVersion)