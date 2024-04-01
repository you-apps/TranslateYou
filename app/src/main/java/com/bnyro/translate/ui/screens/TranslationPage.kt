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

package com.bnyro.translate.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.ui.components.LanguageSelector
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.nav.Destination
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.ui.views.TranslationComponent

@SuppressLint("UnrememberedMutableState")
@Composable
fun TranslationPage(
    navHostController: NavController,
    viewModel: TranslationModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refresh(context)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                mainModel = viewModel,
                menuItems = listOf(
                    MenuItemData(
                        text = stringResource(
                            id = R.string.options
                        ),
                        icon = Icons.Default.Menu
                    ) {
                        navHostController.navigate(Destination.Settings.route)
                    },
                    MenuItemData(
                        text = stringResource(
                            id = R.string.history
                        ),
                        icon = Icons.Default.History
                    ) {
                        navHostController.navigate(Destination.History.route)
                    },
                    MenuItemData(
                        text = stringResource(
                            id = R.string.favorites
                        ),
                        icon = Icons.Default.Favorite
                    ) {
                        navHostController.navigate(Destination.Favorites.route)
                    },
                    MenuItemData(
                        text = stringResource(
                            id = R.string.about
                        ),
                        icon = Icons.Default.Info
                    ) {
                        navHostController.navigate(Destination.About.route)
                    }
                )
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .weight(1.0f)
                ) {
                    TranslationComponent(viewModel)
                }

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
                            viewModel.translateNow()
                        }
                    }

                    val switchBtnEnabled by mutableStateOf(
                        viewModel.sourceLanguage.code.isNotEmpty()
                    )

                    IconButton(
                        onClick = {
                            if (viewModel.availableLanguages.isEmpty()) return@IconButton
                            if (!switchBtnEnabled) return@IconButton
                            val temp = viewModel.sourceLanguage
                            viewModel.sourceLanguage = viewModel.targetLanguage
                            viewModel.targetLanguage = temp

                            if (viewModel.translation.translatedText.isNotEmpty()) {
                                viewModel.insertedText = viewModel.translation.translatedText
                                viewModel.translation = Translation("")
                            }

                            viewModel.translateNow()
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
                            viewModel.translateNow()
                        }
                    }
                }
            }
        }
    }
}
