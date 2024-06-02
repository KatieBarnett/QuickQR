package dev.veryniche.quickqr.purchase

object Products {
    const val PRODUCT_PREFIX = "dev.veryniche.quickqr"
    const val proVersion = "$PRODUCT_PREFIX.pro"
}

fun isProVersionRequired(tileCount: Int) = tileCount >= 1

val appProductList =
    listOf(
        getProductQuery(Products.proVersion)
    )
