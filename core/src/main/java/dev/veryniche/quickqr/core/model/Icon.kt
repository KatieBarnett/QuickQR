package dev.veryniche.quickqr.core.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector

enum class Icon(val vector: ImageVector, val showInList: Boolean = true) {
    ADD_QR_CODE(Icons.Rounded.QrCode2, showInList = false),
    SCAN(Icons.Rounded.QrCodeScanner, showInList = false),
    Web(Icons.Rounded.Web),
    Star(Icons.Rounded.Star),
    Widgets(Icons.Rounded.Widgets),
    Search(Icons.Rounded.Search),
    Home(Icons.Rounded.Home),
    AccountCircle(Icons.Rounded.AccountCircle),
    VideoLibrary(Icons.Rounded.VideoLibrary),
    Visibility(Icons.Rounded.Visibility),
    ShoppingBag(Icons.Rounded.ShoppingBag),
    ShoppingCart(Icons.Rounded.ShoppingCart),
    Lock(Icons.Rounded.Lock),
    Hearing(Icons.Rounded.Hearing),
    FavoriteBorder(Icons.Rounded.FavoriteBorder),
    Favorite(Icons.Rounded.Favorite),
    Face(Icons.Rounded.Face),
    ThumbUp(Icons.Rounded.ThumbUp),
    Language(Icons.Rounded.Language),
    Paid(Icons.Rounded.Paid),
    Help(Icons.Rounded.Help),
    DateRange(Icons.Rounded.DateRange),
    TrendingUp(Icons.Rounded.TrendingUp),
    Savings(Icons.Rounded.Savings),
    QuestionAnswer(Icons.Rounded.QuestionAnswer),
    Route(Icons.Rounded.Route),
    Work(Icons.Rounded.Work),
    Print(Icons.Rounded.Print),
    AccountBalance(Icons.Rounded.AccountBalance),
    ViewInAr(Icons.Rounded.ViewInAr)
}

fun getRandomIcon(): Icon {
    return Icon.entries.filter { it.showInList }.random()
}