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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.bnyro.translate.obj.ListPreferenceOption
import com.bnyro.translate.util.Preferences

@Composable
fun ListPreference(
    title: String,
    summary: String? = null,
    preferenceKey: String,
    defaultValue: String,
    entries: List<String>,
    values: List<String>,
    onOptionSelected: (ListPreferenceOption) -> Unit = {}
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedIndex by remember {
        mutableIntStateOf(values.indexOf(Preferences.get(preferenceKey, defaultValue)))
    }

    PreferenceItem(
        title = title,
        summary = summary ?: entries.getOrElse(selectedIndex) { entries.first() },
        modifier = Modifier.fillMaxWidth()
    ) {
        showDialog = true
    }

    if (showDialog) {
        ListPreferenceDialog(
            preferenceKey = preferenceKey,
            onDismissRequest = {
                showDialog = false
            },
            options = entries.mapIndexed { index, entry ->
                ListPreferenceOption(entry, index)
            },
            onOptionSelected = {
                Preferences.put(preferenceKey, values[it.value])
                selectedIndex = it.value
                onOptionSelected.invoke(it)
            },
            currentValue = selectedIndex
        )
    }
}
