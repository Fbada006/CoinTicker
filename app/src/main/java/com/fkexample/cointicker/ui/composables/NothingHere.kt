package com.fkexample.cointicker.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.fkexample.cointicker.R

@Composable
fun NothingHere() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.size_8))
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.nothing_illustration),
                style = TextStyle(fontSize = dimensionResource(id = R.dimen.font_size_55).value.sp)
            )
            Text(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.size_8))
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.there_is_no_data_to_display_at_the_moment),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}