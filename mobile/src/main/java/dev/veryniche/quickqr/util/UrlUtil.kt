package dev.veryniche.quickqr.util

fun String.resolveUrl() = if (!startsWith("http://") && !startsWith("https://")) {
    "https://$this"
} else {
    this
}
