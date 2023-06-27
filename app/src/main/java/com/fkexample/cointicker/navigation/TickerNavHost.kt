package com.fkexample.cointicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fkexample.cointicker.ui.details.CryptoDetailsScreen
import com.fkexample.cointicker.ui.favorites.FavoritesListScreen
import com.fkexample.cointicker.ui.mainlist.CryptoListScreen
import com.fkexample.cointicker.ui.models.Crypto

@Composable
fun TickerNavHost(
    navController: NavHostController,
    loading: Boolean,
    cryptos: List<Crypto>,
    favCryptos: List<Crypto>,
    onFavoriteClick: (crypto: Crypto) -> Unit,
    onSearch: (query: String) -> Unit,
    onFavListComposableCreated: () -> Unit
) {

    NavHost(navController = navController, startDestination = MainScreen.route) {
        composable(route = MainScreen.route) {
            CryptoListScreen(
                loading = loading,
                cryptos = cryptos,
                onCardClick = { crypto ->
                    navController.navigateToDetailScreen(assetId = crypto.assetId)
                },
                onFavoriteClick = onFavoriteClick,
                onSearch = { query -> onSearch(query) },
                onFilterFavorites = {
                    navController.navigateSingleTopTo(FavoritesScreen.route)
                }
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
        composable(
            route = FavoritesScreen.route
        ) {
            FavoritesListScreen(loading = loading, favCryptos = favCryptos, onCardClick = { crypto ->
                navController.navigateToDetailScreen(assetId = crypto.assetId)
            }, onNavBack = {
                navController.navigateUp()
            }, onFavListComposableCreated = onFavListComposableCreated)
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