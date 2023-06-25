package com.fkexample.cointicker.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface TickerDestinations {
    val route: String
}

object MainScreen : TickerDestinations {
    override val route = "com.fkexample.ticker.mainscreen"
}

object DetailScreen : TickerDestinations {
    override val route = "com.fkexample.ticker.detailscreen"
    const val coinIdArg = "coinAssetId"
    val routeWithArgs = "${route}/{${coinIdArg}}"
    val arguments = listOf(
        navArgument(coinIdArg) { type = NavType.StringType }
    )
}
