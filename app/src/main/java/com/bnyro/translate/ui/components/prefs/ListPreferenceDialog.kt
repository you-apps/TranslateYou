/*
 * Copyright (c) 2023 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.ui.components.prefs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.ui.components.SelectableItem
import com.bnyro.translate.util.Preferences

@Composable
fun ListPreferenceDialog(
    preferenceKey: String?,
    onDismissRequest: () -> Unit,
    options: List<ListPreferenceOption>,
    currentValue: Int? = null,
    title: String? = null,
    onOptionSelected: (ListPreferenceOption) -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            if (title != null)
                Text(title)
        },
        text = {
            LazyColumn {
                items(options) {
                    SelectableItem(
                        text = it.name,
                        onClick = {
                            if (preferenceKey != null) {
                                Preferences.put(
                                    preferenceKey,
                                    it.value.toString()
                                )
                            }
                            onOptionSelected.invoke(it)
                            onDismissRequest.invoke()
                        },
                        isSelected = it.value == currentValue
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
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
