package com.fkexample.cointicker.ui.details

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fkexample.cointicker.R
import com.fkexample.cointicker.ui.composables.CryptoImage
import com.fkexample.cointicker.ui.composables.ErrorDialog
import com.fkexample.cointicker.ui.composables.LoadingCryptoListShimmer
import com.fkexample.cointicker.ui.models.CryptoDetails
import com.fkexample.cointicker.ui.theme.detailsDisplayTitleStyle
import com.fkexample.cointicker.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailsScreen(
    assetId: String,
    loading: Boolean,
    details: CryptoDetails?,
    error: Throwable?,
    onNavBack: () -> Unit,
    getCoinDetails: (assetId: String) -> Unit,
    dismissError: () -> Unit
) {

    LaunchedEffect(key1 = assetId, block = {
        getCoinDetails(assetId)
    })

    val scrollState = rememberScrollState()

    Scaffold(
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
                .padding(paddingValues = paddingValues)
        ) {
            if (loading) {
                LoadingCryptoListShimmer(imageHeight = 200.dp)
            } else {
                Column {
                    if (details != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CryptoImage(imageUrl = details.url)
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = details.name,
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.coin_symbol_label), style = detailsDisplayTitleStyle
                                        )
                                        Text(text = assetId)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stringResource(R.string.last_updated_label), style = detailsDisplayTitleStyle
                                        )
                                        Text(text = DateUtils.getTimeAgo(details.dateCached))
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.usd_price_label), style = detailsDisplayTitleStyle
                                        )
                                        Text(text = details.priceUsd.toString())
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.exchange_rates_label), style = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.euro_exchange_rate_label), style = detailsDisplayTitleStyle
                                        )
                                        Text(text = details.euroToAssetRate.toString())
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.british_pound_exchange_rate_label), style = detailsDisplayTitleStyle
                                        )
                                        Text(text = details.gbpToAssetRate.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }

            error?.let {
                ErrorDialog(dismissError = {
                    onNavBack()
                    dismissError()
                })
            }
        }
    }
}