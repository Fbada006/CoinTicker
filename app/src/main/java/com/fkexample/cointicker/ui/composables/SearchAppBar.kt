package com.fkexample.cointicker.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fkexample.cointicker.R

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    query: String,
    onBackClicked: () -> Unit,
    onClearClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearch: (query: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    value = query,
                    onValueChange = {
                        onSearch(query)
                        onQueryChanged(it)
                    },
                    label = { Text(text = stringResource(id = R.string.search)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        },
                    ),
                    leadingIcon = {
                        IconButton(onClick = {
                            onSearch("")
                            onBackClicked()
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.cd_back_button))
                        }
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onClearClicked()
                                    onSearch("")
                                },
                            ) {
                                Icon(Icons.Rounded.Close, stringResource(id = R.string.cd_close_button))
                            }
                        }
                    }
                )
            }
        }
    }
}