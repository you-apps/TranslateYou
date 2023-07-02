/*
 * Copyright (c) 2023 Bnyro
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

package com.bnyro.translate.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.bnyro.translate.R
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.ui.components.MultiSelectList
import com.bnyro.translate.util.Preferences

@Composable
fun EngineSelectionDialog(
    onDismissRequest: () -> Unit
) {
    val simEngines = TranslationEngines.engines.filter {
        it.supportsSimTranslation
    }

    var selectedItems by remember {
        mutableStateOf(
            simEngines.map {
                it.isSimultaneousTranslationEnabled()
            }.toBooleanArray()
        )
    }

    AlertDialog(
        title = {
            Text(stringResource(R.string.enabled_engines))
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    simEngines.forEachIndexed { index, it ->
                        Preferences.put(
                            it.simPrefKey,
                            selectedItems[index]
                        )
                    }
                    onDismissRequest.invoke()
                }
            ) {
                Text(stringResource(R.string.okay))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            MultiSelectList(
                titles = simEngines.filter {
                    it.supportsSimTranslation
                }.map { it.name },
                selectedItems = selectedItems,
                onChange = {
                    selectedItems = it
                }
            )
        }
    )
}
