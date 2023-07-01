package com.fkexample.cointicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
            val cryptos by cryptoViewModel.cryptos.collectAsState()
            val favCryptos by cryptoViewModel.favCryptos.collectAsState()
            val loading by cryptoViewModel.isLoading.collectAsState()
            val isNetworkAvailable by connectivityManager.isNetworkAvailable
            val details by cryptoViewModel.detailCryptosState.collectAsState()
            val error by cryptoViewModel.error.collectAsState()

            CoinTickerTheme(isNetworkAvailable = isNetworkAvailable) {
                TickerNavHost(
                    navController = navController,
                    loading = loading,
                    cryptos = cryptos,
                    onFavoriteClick = { crypto -> cryptoViewModel.onFavouriteClick(crypto) },
                    onSearch = { query -> cryptoViewModel.onSearch(query) },
                    favCryptos = favCryptos,
                    onFavListComposableCreated = { cryptoViewModel.getAllFavoriteCoins() },
                    details = details,
                    getCoinDetails = { assetId -> cryptoViewModel.getCoinDetails(assetId) },
                    error = error,
                    dismissError = { cryptoViewModel.dismissError() }
                )
            }
        }
    }
}
