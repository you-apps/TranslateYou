/*
 * Copyright (c) 2025 You Apps
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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.translate.ui.components.BlockButton
import com.bnyro.translate.util.Preferences

@Composable
fun DropDownSelectPreference(
    preferenceKey: String,
    title: String,
    onSelect: (Int) -> Unit,
    items: List<String>,
) {
    var selected by remember {
        mutableIntStateOf(Preferences.get(preferenceKey, 0))
    }

    var showDropDown by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        PreferenceItem(title = title, summary = items[selected]) {
            showDropDown = true
        }

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(item)
                    },
                    onClick = {
                        Preferences.put(preferenceKey, index)

                        selected = index
                        onSelect(index)

                        showDropDown = false
                    }
                )
            }
        }
    }
}
