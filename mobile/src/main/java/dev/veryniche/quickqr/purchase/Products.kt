package dev.veryniche.quickqr.purchase

object Products {
    const val proVersion = "dev.veryniche.quickqr.pro"
}

fun isProVersionRequired(tileCount: Int) = tileCount >= 1