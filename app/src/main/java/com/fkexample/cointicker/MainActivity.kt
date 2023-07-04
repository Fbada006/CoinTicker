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
            val cryptos by cryptoViewModel.cryptos.collectAsStateWithLifecycle()
            val favCryptos by cryptoViewModel.favCryptos.collectAsStateWithLifecycle()
            val loading by cryptoViewModel.isLoading.collectAsStateWithLifecycle()
            val isNetworkAvailable by connectivityManager.isNetworkAvailable
            val details by cryptoViewModel.detailCryptosState.collectAsStateWithLifecycle()
            val error by cryptoViewModel.error.collectAsStateWithLifecycle()

            CoinTickerTheme(isNetworkAvailable = isNetworkAvailable) {
                TickerNavHost(
                    navController = navController,
                    loading = loading,
                    cryptos = cryptos,
                    onFavoriteClick = { crypto -> cryptoViewModel.onFavouriteClick(crypto) },
                    onSearch = { query -> cryptoViewModel.onSearch(query) },
                    favCryptos = favCryptos,
                    getAllFavs = { cryptoViewModel.getAllFavoriteCoins() },
                    details = details,
                    getCoinDetails = { assetId -> cryptoViewModel.getCoinDetails(assetId) },
                    error = error,
                    dismissError = { cryptoViewModel.dismissError() }
                )
            }
        }
    }
}
