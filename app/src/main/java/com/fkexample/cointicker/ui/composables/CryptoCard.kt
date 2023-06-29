package com.fkexample.cointicker.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.fkexample.cointicker.R
import com.fkexample.cointicker.ui.models.Crypto
import com.fkexample.cointicker.utils.DateUtils

@Composable
fun CryptoCard(
    crypto: Crypto,
    shouldShowFavIcon: Boolean = true,
    onCardClick: () -> Unit,
    onFavoriteClick: (crypto: Crypto) -> Unit = {},
    modifier: Modifier
) {

    Card(
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.size_8)),
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.size_6))
            .clickable {
                onCardClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16))
        ) {
            CryptoImage(imageUrl = crypto.cryptoUrl)
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_16)))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = crypto.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.size_4)))
                Text(
                    text = stringResource(R.string.last_updated, DateUtils.getTimeAgo(crypto.dateCached)),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
            }

            if (shouldShowFavIcon) {
                LikeToggleButton(isFav = crypto.isFavorite, onFavorite = { onFavoriteClick(crypto) })
            }
        }
    }
}
