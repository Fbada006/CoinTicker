package com.fkexample.cointicker.ui.details

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R
import com.fkexample.cointicker.ui.CryptoImage
import com.fkexample.cointicker.ui.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.models.CryptoDetails
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailsScreen(
    assetId: String,
    loading: Boolean,
    details: CryptoDetails?,
    error: Throwable?,
    onNavBack: () -> Unit,
    getCoinDetails: (assetId: String) -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        getCoinDetails(assetId)
    })

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavBack) {
                        Icon(Icons.Rounded.ArrowBack, stringResource(id = R.string.cd_close_button))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.asset_id_details_label))
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(state = scrollState, orientation = Orientation.Vertical)
                .padding(paddingValues = paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                LoadingCryptoListShimmer(imageHeight = 200.dp)
            } else {
                Timber.e("Details ----------      $details")
                Column {
                    if (details != null) {
                        CryptoImage(imageUrl = details.url)
                        Text(text = details.name)
                        Text(text = details.dateCached.toString())
                        Text(text = details.url.toString())
                    }
                }
            }

            error?.let {
                LaunchedEffect(key1 = snackbarHostState, block = {
                    snackbarHostState.showSnackbar(context.getString(R.string.something_went_wrong_please_try_again))
                })
            }
        }
    }
}