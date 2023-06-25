package com.fkexample.cointicker.ui.mainlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.ui.CryptoCard
import com.fkexample.cointicker.ui.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.NothingHere

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoListScreen(
    loading: Boolean,
    cryptos: List<Crypto>,
    onCardClick: (crypto: Crypto) -> Unit,
    onFavoriteClick: (crypto: Crypto) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            if (loading && cryptos.isEmpty()) {
                LoadingCryptoListShimmer(imageHeight = 200.dp)
            } else if (cryptos.isEmpty()) {
                NothingHere()
            } else {
                LazyColumn(
                    state = rememberLazyListState()
                ) {
                    itemsIndexed(
                        items = cryptos
                    ) { _, crypto ->
                        CryptoCard(crypto = crypto, onCardClick = { onCardClick(crypto) }, onFavoriteClick = { onFavoriteClick(crypto) })
                    }
                }
            }
        }
    }
}