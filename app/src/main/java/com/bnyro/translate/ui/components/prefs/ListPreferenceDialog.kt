package com.bnyro.translate.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.util.Preferences

@Composable
fun ListPreferenceDialog(
    preferenceKey: String,
    onDismissRequest: () -> Unit,
    options: List<ListPreferenceOption>,
    currentValue: Int? = null,
    onOptionSelected: (ListPreferenceOption) -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            LazyColumn {
                items(options) {
                    SelectableItem(
                        text = if (it.value == currentValue) "${it.name}   ✓" else it.name,
                        onClick = {
                            Preferences.put(
                                preferenceKey,
                                it.value.toString()
                            )
                            onOptionSelected.invoke(it)
                            onDismissRequest.invoke()
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text(
                    stringResource(
                        R.string.cancel
                    )
                )
            }
        }
    )
}
