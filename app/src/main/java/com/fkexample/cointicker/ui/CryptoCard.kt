package com.fkexample.cointicker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R
import com.fkexample.cointicker.presentation.models.Crypto

@Composable
fun CryptoCard(crypto: Crypto, onCardClick: () -> Unit, onFavoriteClick: () -> Unit, modifier: Modifier) {

    val interactionSource = remember { MutableInteractionSource() }
    val rippleColor = rememberRipple(bounded = false)

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(6.dp)
            .clickable {
                onCardClick()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            CryptoImage(imageUrl = crypto.cryptoUrl)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = crypto.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.last_updated, crypto.timeAgo),
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
            }
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(R.string.cd_favorite_icon),
                modifier = Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = rippleColor,
                    onClick = onFavoriteClick
                )
            )
        }
    }
}
