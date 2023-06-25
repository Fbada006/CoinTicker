package com.fkexample.cointicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.fkexample.cointicker.network.utils.ConnectionManager
import com.fkexample.cointicker.ui.mainlist.CryptoListScreen
import com.fkexample.cointicker.ui.mainlist.CryptoListViewModel
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
            val viewModel: CryptoListViewModel = hiltViewModel()
            val cryptos = viewModel.cryptos.value
            val loading = viewModel.isLoading.value
            val isNetworkAvailable = connectivityManager.isNetworkAvailable.value

            CryptoListScreen(loading = loading, isNetworkAvailable = isNetworkAvailable, cryptos = cryptos, onCardClick = {}, onFavoriteClick = {})
        }
    }
}

