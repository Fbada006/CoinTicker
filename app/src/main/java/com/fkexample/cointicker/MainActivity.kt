package com.fkexample.cointicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.fkexample.cointicker.network.utils.ConnectionManager
import com.fkexample.cointicker.ui.mainlist.CryptoListViewModel
import com.fkexample.cointicker.ui.theme.CoinTickerTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
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
            val scroll = rememberScrollState(0)

            CoinTickerTheme {
                Timber.d("The network state is: ----- ${connectivityManager.isNetworkAvailable.value}")
                Timber.d("Is this a loading state: ----- ${viewModel.isLoading.value}")
                Timber.d("The size of the crypto list is : ----- ${viewModel.cryptos.value.size}")
                val cryptos = viewModel.cryptos.value.joinToString(separator = "\n\n")
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scroll), color = MaterialTheme.colorScheme.background) {
                    Greeting(cryptos)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}
