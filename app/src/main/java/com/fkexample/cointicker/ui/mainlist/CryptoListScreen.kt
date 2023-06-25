package com.fkexample.cointicker.ui.mainlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R
import com.fkexample.cointicker.presentation.models.Crypto
import com.fkexample.cointicker.ui.CryptoCard
import com.fkexample.cointicker.ui.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.NothingHere
import com.fkexample.cointicker.ui.SearchAppBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CryptoListScreen(
    loading: Boolean,
    cryptos: List<Crypto>,
    onCardClick: (crypto: Crypto) -> Unit,
    onFavoriteClick: (crypto: Crypto) -> Unit,
    onSearch: (query: String) -> Unit
) {

    var shouldShowSearch by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = !shouldShowSearch,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ), exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                )
            ) {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    actions = {
                        IconButton(
                            onClick = { shouldShowSearch = true },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp)
                        ) {
                            Icon(Icons.Rounded.Search, stringResource(id = R.string.search))
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = shouldShowSearch,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ), exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                )
            ) {
                SearchAppBar(
                    query = query,
                    onBackClicked = { shouldShowSearch = false },
                    onQueryChanged = { value -> query = value },
                    onSearch = onSearch,
                    onClearClicked = { query = "" }
                )
            }
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