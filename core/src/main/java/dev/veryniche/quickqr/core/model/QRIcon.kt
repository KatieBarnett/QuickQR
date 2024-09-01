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
import dev.veryniche.quickqr.core.R
import kotlinx.serialization.Serializable

@Serializable
enum class QRIcon(val vector: ImageVector, @DrawableRes val drawableId: Int, val showInList: Boolean = true,) {
    ADD_QR_CODE(
        Icons.Rounded.QrCode2,
        showInList = false,
        drawableId = R.drawable.qr_code_2_24dp_e8eaed_fill1_wght400_grad0_opsz24
    ),
    SCAN(
        Icons.Rounded.QrCodeScanner,
        showInList = false,
        drawableId = R.drawable.qr_code_scanner_24dp_e8eaed_fill1_wght400_grad0_opsz24
    ),
    Web(Icons.Rounded.Web, R.drawable.web_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Star(Icons.Rounded.Star, R.drawable.star_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Widgets(Icons.Rounded.Widgets, R.drawable.widgets_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Search(Icons.Rounded.Search, R.drawable.search_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Home(Icons.Rounded.Home, R.drawable.home_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    AccountCircle(Icons.Rounded.AccountCircle, R.drawable.account_circle_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    VideoLibrary(Icons.Rounded.VideoLibrary, R.drawable.video_library_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Visibility(Icons.Rounded.Visibility, R.drawable.visibility_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    ShoppingBag(Icons.Rounded.ShoppingBag, R.drawable.shopping_bag_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    ShoppingCart(Icons.Rounded.ShoppingCart, R.drawable.shopping_cart_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Lock(Icons.Rounded.Lock, R.drawable.lock_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Hearing(Icons.Rounded.Hearing, R.drawable.hearing_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    FavoriteBorder(Icons.Rounded.FavoriteBorder, R.drawable.favorite_24dp_e8eaed_fill0_wght400_grad0_opsz24),
    Favorite(Icons.Rounded.Favorite, R.drawable.favorite_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Face(Icons.Rounded.Face, R.drawable.face_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    ThumbUp(Icons.Rounded.ThumbUp, R.drawable.thumb_up_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Language(Icons.Rounded.Language, R.drawable.language_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Paid(Icons.Rounded.Paid, R.drawable.paid_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Help(Icons.Rounded.Help, R.drawable.help_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    DateRange(Icons.Rounded.DateRange, R.drawable.date_range_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    TrendingUp(Icons.Rounded.TrendingUp, R.drawable.trending_up_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Savings(Icons.Rounded.Savings, R.drawable.savings_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    QuestionAnswer(Icons.Rounded.QuestionAnswer, R.drawable.forum_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Route(Icons.Rounded.Route, R.drawable.route_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Work(Icons.Rounded.Work, R.drawable.work_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Print(Icons.Rounded.Print, R.drawable.print_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    AccountBalance(Icons.Rounded.AccountBalance, R.drawable.account_balance_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    ViewInAr(Icons.Rounded.ViewInAr, R.drawable.view_in_ar_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    HourglassBottom(Icons.Rounded.HourglassBottom, R.drawable.hourglass_bottom_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SmartToyBeer(Icons.Rounded.SmartToy, R.drawable.smart_toy_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    DriveEta(Icons.Rounded.DriveEta, R.drawable.directions_car_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    BugReport(Icons.Rounded.BugReport, R.drawable.bug_report_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Watch(Icons.Rounded.Watch, R.drawable.watch_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SportsRugby(Icons.Rounded.SportsRugby, R.drawable.sports_rugby_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SportsBasketball(
        Icons.Rounded.SportsBasketball,
        R.drawable.sports_basketball_24dp_e8eaed_fill1_wght400_grad0_opsz24
    ),
    SportsBaseball(Icons.Rounded.SportsBaseball, R.drawable.sports_baseball_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SportsCricket(Icons.Rounded.SportsCricket, R.drawable.sports_cricket_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SportsSoccer(Icons.Rounded.SportsSoccer, R.drawable.sports_soccer_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    SportsBar(Icons.Rounded.SportsBar, R.drawable.sports_bar_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    LocalBar(Icons.Rounded.LocalBar, R.drawable.local_bar_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Coffee(Icons.Rounded.Coffee, R.drawable.coffee_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Liquor(Icons.Rounded.Liquor, R.drawable.liquor_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    WineBar(Icons.Rounded.WineBar, R.drawable.wine_bar_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    EmojiFoodBeverage(
        Icons.Rounded.EmojiFoodBeverage,
        R.drawable.emoji_food_beverage_24dp_e8eaed_fill1_wght400_grad0_opsz24
    ),
    LocalPizza(Icons.Rounded.LocalPizza, R.drawable.local_pizza_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    BreakfastDining(Icons.Rounded.BreakfastDining, R.drawable.breakfast_dining_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    BrunchDining(Icons.Rounded.BrunchDining, R.drawable.brunch_dining_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    LunchDining(Icons.Rounded.LunchDining, R.drawable.lunch_dining_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    RamenDining(Icons.Rounded.RamenDining, R.drawable.ramen_dining_24dp_e8eaed_fill1_wght400_grad0_opsz24),
    Fastfood(Icons.Rounded.Fastfood, R.drawable.fastfood_24dp_e8eaed_fill1_wght400_grad0_opsz24),
}

fun getRandomIcon(): QRIcon {
    return QRIcon.entries.filter { it.showInList }.random()
}
