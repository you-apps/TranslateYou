package com.bnyro.translate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bnyro.translate.R
import com.bnyro.translate.models.OptionsModel
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsDialog(
    viewModel: OptionsModel = viewModel()
) {
    var instanceUrl by remember {
        mutableStateOf(
            Preferences.get(
                Preferences.instanceUrlKey,
                Preferences.defaultInstanceUrl
            )
        )
    }

    var apiKey by remember {
        mutableStateOf(
            Preferences.get(
                Preferences.apiKey,
                ""
            )
        )
    }

    AlertDialog(
        onDismissRequest = {
            viewModel.openDialog = false
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
                OutlinedTextField(
                    value = instanceUrl,
                    onValueChange = { instanceUrl = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.instance
                            )
                        )
                    }
                )

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.api_key
                            )
                        )
                    },
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 0.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Preferences.put(
                        Preferences.instanceUrlKey,
                        instanceUrl
                    )
                    RetrofitInstance.createApi()
                    viewModel.openDialog = false
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
                    viewModel.openDialog = false
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
