package com.fkexample.cointicker.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fkexample.cointicker.R

@Composable
fun ErrorDialog(
    dismissError: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismissError,
        title = { Text(stringResource(id = R.string.error)) },
        text = { Text(stringResource(id = R.string.something_went_wrong_please_try_again)) },
        confirmButton = {
            TextButton(onClick = dismissError) {
                Text(stringResource(id = R.string.okay))
            }
        }
    )
}