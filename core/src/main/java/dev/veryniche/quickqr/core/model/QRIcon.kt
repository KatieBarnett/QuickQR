package dev.veryniche.quickqr.core.model

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.BreakfastDining
import androidx.compose.material.icons.rounded.BrunchDining
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.DriveEta
import androidx.compose.material.icons.rounded.EmojiFoodBeverage
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Liquor
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.LocalPizza
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LunchDining
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Print
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.QuestionAnswer
import androidx.compose.material.icons.rounded.RamenDining
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.SmartToy
import androidx.compose.material.icons.rounded.SportsBar
import androidx.compose.material.icons.rounded.SportsBaseball
import androidx.compose.material.icons.rounded.SportsBasketball
import androidx.compose.material.icons.rounded.SportsCricket
import androidx.compose.material.icons.rounded.SportsRugby
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.Watch
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.WineBar
import androidx.compose.material.icons.rounded.Work
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
enum class QRIcon(val vector: ImageVector, val showInList: Boolean = true, @DrawableRes val drawableId: Int) {
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
    ViewInAr(Icons.Rounded.ViewInAr),
    HourglassBottom(Icons.Rounded.HourglassBottom),
    SmartToyBeer(Icons.Rounded.SmartToy),
    DriveEta(Icons.Rounded.DriveEta),
    BugReport(Icons.Rounded.BugReport),
    Watch(Icons.Rounded.Watch),
    SportsRugby(Icons.Rounded.SportsRugby),
    SportsBasketball(Icons.Rounded.SportsBasketball),
    SportsBaseball(Icons.Rounded.SportsBaseball),
    SportsCricket(Icons.Rounded.SportsCricket),
    SportsSoccer(Icons.Rounded.SportsSoccer),
    SportsBar(Icons.Rounded.SportsBar),
    LocalBar(Icons.Rounded.LocalBar),
    Coffee(Icons.Rounded.Coffee),
    Liquor(Icons.Rounded.Liquor),
    WineBar(Icons.Rounded.WineBar),
    EmojiFoodBeverage(Icons.Rounded.EmojiFoodBeverage),
    LocalPizza(Icons.Rounded.LocalPizza),
    BreakfastDining(Icons.Rounded.BreakfastDining),
    BrunchDining(Icons.Rounded.BrunchDining),
    LunchDining(Icons.Rounded.LunchDining),
    RamenDining(Icons.Rounded.RamenDining),
    Fastfood(Icons.Rounded.Fastfood),
}

fun getRandomIcon(): QRIcon {
    return QRIcon.entries.filter { it.showInList }.random()
}
