package com.bnyro.translate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.util.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsDialog(
    onDismissRequest: () -> Unit
) {
    var instanceUrl by remember {
        mutableStateOf(
            RetrofitInstance.url
        )
    }

    AlertDialog(
        onDismissRequest = {
            onDismissRequest.invoke()
        },
        title = {
            Text(
                text = stringResource(
                    id = R.string.options
                )
            )
        },
        text = {
            Column() {
                TextField(
                    value = instanceUrl,
                    onValueChange = { instanceUrl = it },
                    placeholder = {
                        Text(
                            text = stringResource(
                                id = R.string.instance
                            )
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    RetrofitInstance.url = instanceUrl
                    RetrofitInstance.createApi()
                    onDismissRequest.invoke()
                }
            ) {
                Text(
                    text = stringResource(
                        id = R.string.okay
                    )
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text(
                    text = stringResource(
                        id = R.string.cancel
                    )
                )
            }
        }
    )
}
