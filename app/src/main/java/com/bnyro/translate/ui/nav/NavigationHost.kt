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

package com.bnyro.translate.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.screens.HistoryScreen
import com.bnyro.translate.ui.screens.SettingsScreen
import com.bnyro.translate.ui.screens.TranslationPage
import com.bnyro.translate.ui.views.AboutPage

@Composable
fun NavigationHost(
    navController: NavHostController,
    translationModel: TranslationModel
) {
    NavHost(navController = navController, Destination.Translate.route) {
        composable(Destination.Translate.route) {
            TranslationPage(navController, translationModel)
        }
        composable(Destination.History.route) {
            HistoryScreen(navController, translationModel)
        }
        composable(Destination.Settings.route) {
            SettingsScreen(navController)
        }
        composable(Destination.About.route) {
            AboutPage(navController)
        }
    }
}
