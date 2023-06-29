package com.fkexample.cointicker.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.fkexample.cointicker.R

@Composable
fun CryptoImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        loading = {
            Box(contentAlignment = Alignment.Center) {
                LoadingItem()
            }
        },
        error = {
            Image(
                painterResource(id = R.drawable.ic_money),
                contentDescription = stringResource(id = R.string.cd_error_image),
                contentScale = ContentScale.Crop
            )
        },
        contentDescription = stringResource(R.string.cd_card_holding_the_crypto_data),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}