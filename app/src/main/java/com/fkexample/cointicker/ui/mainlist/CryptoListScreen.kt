package com.fkexample.cointicker.ui.mainlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
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
import com.fkexample.cointicker.ui.composables.CryptoCard
import com.fkexample.cointicker.ui.composables.ErrorDialog
import com.fkexample.cointicker.ui.composables.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.composables.NothingHere
import com.fkexample.cointicker.ui.composables.SearchAppBar
import com.fkexample.cointicker.ui.models.Crypto

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CryptoListScreen(
    loading: Boolean,
    cryptos: List<Crypto>,
    error: Throwable?,
    onCardClick: (crypto: Crypto) -> Unit,
    onFavoriteClick: (crypto: Crypto) -> Unit,
    onSearch: (query: String) -> Unit,
    onFilterFavorites: () -> Unit,
    dismissError: () -> Unit
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
                            onClick = onFilterFavorites,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 8.dp)
                        ) {
                            Icon(Icons.Rounded.Favorite, stringResource(id = R.string.cd_favorite_icon), tint = MaterialTheme.colorScheme.tertiary)
                        }
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
                    onBackClicked = {
                        query = ""
                        shouldShowSearch = false
                    },
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
                    state = rememberLazyListState(),
                ) {
                    items(
                        items = cryptos,
                        key = { crypto -> crypto.assetId }
                    ) { crypto ->
                        CryptoCard(
                            crypto = crypto,
                            onCardClick = { onCardClick(crypto) },
                            onFavoriteClick = onFavoriteClick,
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
            }

            error?.let {
                ErrorDialog(dismissError = dismissError)
            }
        }
    }
}