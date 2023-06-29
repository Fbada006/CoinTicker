package com.fkexample.cointicker.ui.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R

/**
 *The LikeToggleButton propagates state up to the parent so that the whole row can be updated properly
 *
 * @param isFav is the value to be used to determine the icon to show
 * @param onFavorite is the function to handle the favorite feature
 * */
@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun LikeToggleButton(
    isFav: Boolean,
    onFavorite: () -> Unit,
) {

    val checkedState = remember { mutableStateOf(isFav) }

    IconToggleButton(
        checked = checkedState.value,
        onCheckedChange = {
            onFavorite()
            checkedState.value = !checkedState.value
        }
    ) {

        val transition = updateTransition(checkedState.value, label = stringResource(id = R.string.toggle_label))

        val size by transition.animateDp(
            transitionSpec = {
                if (false isTransitioningTo true) {
                    keyframes {
                        durationMillis = 250
                        30.dp at 0 with LinearOutSlowInEasing
                        35.dp at 15 with FastOutLinearInEasing
                        40.dp at 75
                        35.dp at 150
                    }
                } else {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
            },
            label = stringResource(id = R.string.toggle_size),
            targetValueByState = { dimensionResource(id = R.dimen.size_25) }
        )

        val tint = if (isFav) MaterialTheme.colorScheme.tertiary else Color.Unspecified

        Icon(
            imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourite),
            tint = tint,
            modifier = Modifier.size(size)
        )
    }
}