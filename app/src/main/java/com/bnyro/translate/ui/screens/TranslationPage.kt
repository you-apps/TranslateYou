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
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.bnyro.translate.R
import com.bnyro.translate.obj.MenuItemData
import com.bnyro.translate.ui.components.TranslationFooter
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.nav.Destination
import com.bnyro.translate.ui.views.AdditionalInfoComponent
import com.bnyro.translate.ui.views.TopBar
import com.bnyro.translate.ui.views.TranslationComponent
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.ui.views.SimTranslationComponent

@SuppressLint("UnrememberedMutableState")
@Composable
fun TranslationPage(
    navHostController: NavController,
    viewModel: TranslationModel
) {
    val context = LocalContext.current
    val view = LocalView.current

    var isKeyboardOpen by remember {
        mutableStateOf(false)
    }
    // detect whether the keyboard is open or closed
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

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
    ) {
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
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        TranslationComponent(Modifier.weight(1f), viewModel)

                        if (Preferences.get(Preferences.showAdditionalInfo, true)
                            && !isKeyboardOpen
                        ) {
                            AdditionalInfoComponent(viewModel.translation, viewModel)
                        }

                        Spacer(
                            modifier = Modifier
                                .height(15.dp)
                        )

                        if (viewModel.simTranslationEnabled) {
                            Log.e("sim tra", "sim")
                            SimTranslationComponent(viewModel)
                        } else {
                            HorizontalDivider(
                                color = Color.Gray,
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterHorizontally)
                                    .size(70.dp, 2.dp)
                            )
                        }
                    }
                }

                TranslationFooter(viewModel = viewModel)
            }
        }
    }
}
