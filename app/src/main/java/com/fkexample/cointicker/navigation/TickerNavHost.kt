package com.fkexample.cointicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.ui.details.CryptoDetailsScreen
import com.fkexample.cointicker.ui.mainlist.CryptoListScreen

@Composable
fun TickerNavHost(
    navController: NavHostController,
    loading: Boolean,
    cryptos: List<Crypto>,
    onFavoriteClick: (crypto: Crypto) -> Unit,
) {

    NavHost(navController = navController, startDestination = MainScreen.route) {
        composable(route = MainScreen.route) {
            CryptoListScreen(
                loading = loading,
                cryptos = cryptos,
                onCardClick = { crypto ->
                    navController.navigateToDetailScreen(crypto.assetId)
                },
                onFavoriteClick = { crypto -> onFavoriteClick(crypto) }
            )
        }
        composable(
            route = DetailScreen.routeWithArgs,
            arguments = DetailScreen.arguments
        ) { navBackStackEntry ->
            val assetId =
                navBackStackEntry.arguments?.getString(DetailScreen.coinIdArg)

            CryptoDetailsScreen(assetId = assetId, onNavBack = { navController.navigateUp() })
        }
    }
}

private fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

fun NavHostController.navigateToDetailScreen(assetId: String) {
    this.navigateSingleTopTo("${DetailScreen.route}/$assetId")
}