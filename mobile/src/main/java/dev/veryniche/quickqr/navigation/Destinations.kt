package dev.veryniche.quickqr.navigation

sealed class Destinations(val route: String)

data object Home : Destinations("home")
data object ExpandedCode : Destinations("expandedCode/{id}") {
    fun getExactRoute(id: Int) = "expandedCode/$id"
}
