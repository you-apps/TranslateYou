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

package com.bnyro.translate.util

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.ui.models.TranslationModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimTranslationComponent(
    viewModel: TranslationModel
) {
    var selected by remember {
        mutableStateOf(
            viewModel.engine
        )
    }

    LazyRow {
        items(viewModel.enabledSimEngines) {
            ElevatedFilterChip(
                selected = selected == it,
                onClick = {
                    selected = it
                    viewModel.engine = it
                    viewModel.translation = viewModel.translatedTexts[it.name] ?: Translation("")
                },
                label = {
                    Text(it.name)
                },
                modifier = Modifier
                    .padding(5.dp, 0.dp)
            )
        }
    }
}
