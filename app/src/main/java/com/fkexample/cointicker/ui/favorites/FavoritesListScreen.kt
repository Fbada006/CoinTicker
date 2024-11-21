package com.fkexample.cointicker.ui.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.fkexample.cointicker.R
import com.fkexample.cointicker.ui.composables.CryptoCard
import com.fkexample.cointicker.ui.composables.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.composables.NothingHere
import com.fkexample.cointicker.ui.models.Crypto

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoritesListScreen(
    loading: Boolean,
    favCryptos: List<Crypto>,
    onCardClick: (crypto: Crypto) -> Unit,
    onNavBack: () -> Unit,
    getAllFavs: () -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        getAllFavs()
    })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.favourites_label))
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    IconButton(
                        onClick = onNavBack,
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(id = R.string.cd_back_button))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
        ) {
            if (loading && favCryptos.isEmpty()) {
                LoadingCryptoListShimmer(imageHeight = dimensionResource(id = R.dimen.size_200))
            } else if (favCryptos.isEmpty()) {
                NothingHere()
            } else {
                LazyColumn(
                    state = rememberLazyListState(),
                ) {
                    items(
                        items = favCryptos,
                        key = { crypto -> crypto.assetId }
                    ) { crypto ->
                        CryptoCard(
                            crypto = crypto,
                            onCardClick = { onCardClick(crypto) },
                            shouldShowFavIcon = false,
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}