/*
 * Copyright (c) 2024 You Apps
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

package com.bnyro.translate.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.ui.models.TranslationModel

@Composable
fun LanguageSelectionComponent(viewModel: TranslationModel) {
    val orientation = LocalConfiguration.current.orientation
    val context = LocalContext.current

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector(
                    viewModel.availableLanguages,
                    viewModel.sourceLanguage,
                    autoLanguageEnabled = viewModel.engine.autoLanguageCode != null,
                    viewModel = viewModel
                ) {
                    if (it == viewModel.targetLanguage) {
                        viewModel.targetLanguage = viewModel.sourceLanguage
                    }
                    viewModel.sourceLanguage = it
                    viewModel.translateNow(context)
                }
            }

            SwapLanguagesButton(viewModel)

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector(
                    viewModel.availableLanguages,
                    viewModel.targetLanguage,
                    viewModel = viewModel
                ) {
                    if (it == viewModel.sourceLanguage) {
                        viewModel.sourceLanguage = viewModel.targetLanguage
                    }
                    viewModel.targetLanguage = it
                    viewModel.translateNow(context)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .padding(top = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector(
                    viewModel.availableLanguages,
                    viewModel.sourceLanguage,
                    autoLanguageEnabled = viewModel.engine.autoLanguageCode != null,
                    viewModel = viewModel
                ) {
                    if (it == viewModel.targetLanguage) {
                        viewModel.targetLanguage = viewModel.sourceLanguage
                    }
                    viewModel.sourceLanguage = it
                    viewModel.translateNow(context)
                }
            }

            SwapLanguagesButton(viewModel)

            Box(
                contentAlignment = Alignment.Center
            ) {
                LanguageSelector(
                    viewModel.availableLanguages,
                    viewModel.targetLanguage,
                    viewModel = viewModel
                ) {
                    if (it == viewModel.sourceLanguage) {
                        viewModel.sourceLanguage = viewModel.targetLanguage
                    }
                    viewModel.targetLanguage = it
                    viewModel.translateNow(context)
                }
            }
        }
    }
}

@Composable
fun SwapLanguagesButton(viewModel: TranslationModel) {
    val context = LocalContext.current
    var switchBtnEnabled by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        viewModel.availableLanguages,
        viewModel.sourceLanguage,
        viewModel.translation,
        viewModel.translatedTexts
    ) {
        // also enable the switch languages button if the translator detected a source language
        switchBtnEnabled =
            viewModel.sourceLanguage.code.isNotEmpty() || viewModel.mostDetectedLanguage() != null
    }

    IconButton(
        onClick = {
            if (switchBtnEnabled) viewModel.swapLanguages(context)
        }
    ) {
        Icon(
            painterResource(R.drawable.ic_switch),
            null,
            modifier = Modifier
                .size(18.dp),
            tint = if (switchBtnEnabled) MaterialTheme.colorScheme.onSurface else Color.Gray
        )
    }
}