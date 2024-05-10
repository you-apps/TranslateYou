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

package com.bnyro.translate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.LocaleHelper
import com.bnyro.translate.util.Preferences

open class BaseActivity: ComponentActivity() {
    lateinit var translationModel: TranslationModel
    var themeMode by mutableStateOf(Preferences.getThemeMode())
    var accentColor by mutableStateOf(Preferences.getAccentColor())

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.updateLanguage(this)

        translationModel = ViewModelProvider(this)[TranslationModel::class.java]

        super.onCreate(savedInstanceState)
    }

    fun showContent(content: @Composable () -> Unit) {
        setContent {
            TranslateYouTheme(themeMode, accentColor?.hexToColor()) {
                content()
            }
        }
    }
}