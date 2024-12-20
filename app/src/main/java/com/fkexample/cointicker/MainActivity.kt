package com.fkexample.cointicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.fkexample.cointicker.navigation.TickerNavHost
import com.fkexample.cointicker.network.utils.ConnectionManager
import com.fkexample.cointicker.ui.CryptoViewModel
import com.fkexample.cointicker.ui.theme.CoinTickerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityManager: ConnectionManager

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cryptoViewModel: CryptoViewModel = hiltViewModel()
            val state by cryptoViewModel.state.collectAsStateWithLifecycle()
            val isNetworkAvailable by connectivityManager.isNetworkAvailable

            CoinTickerTheme(isNetworkAvailable = isNetworkAvailable) {
                TickerNavHost(
                    navController = navController,
                    loading = state.isLoading,
                    cryptos = state.cryptos,
                    onFavoriteClick = { crypto -> cryptoViewModel.onFavouriteClick(crypto) },
                    onSearch = { query -> cryptoViewModel.onSearch(query) },
                    favCryptos = state.favCryptos,
                    getAllFavs = { cryptoViewModel.getAllFavoriteCoins() },
                    details = state.cryptoDetails,
                    getCoinDetails = { assetId -> cryptoViewModel.getCoinDetails(assetId) },
                    error = state.error,
                    dismissError = { cryptoViewModel.dismissError() }
                )
            }
        }
    }
}
