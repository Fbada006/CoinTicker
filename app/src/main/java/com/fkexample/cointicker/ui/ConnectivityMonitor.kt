package com.fkexample.cointicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R

@Composable
fun ConnectivityMonitor(
    isNetworkAvailable: Boolean,
) {
    if (!isNetworkAvailable) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)) {
            Text(
                stringResource(R.string.no_network_connection),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}