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

package com.bnyro.translate.ui.models

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bnyro.translate.obj.TessLanguage
import com.bnyro.translate.util.TessHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TessModel: ViewModel() {
    var availableLanguages by mutableStateOf(emptyList<TessLanguage>())
    var downloadedLanguages by mutableStateOf(emptyList<String>())
    var notYetDownloadedLanguages by mutableStateOf(emptyList<TessLanguage>())

    fun init(context: Context) {
        downloadedLanguages = TessHelper.getDownloadedLanguages(context)

        viewModelScope.launch(Dispatchers.IO) {
            availableLanguages = TessHelper.getAvailableLanguages()
        }
    }

    fun refreshDownloadedLanguages(context: Context) {
        downloadedLanguages = TessHelper.getDownloadedLanguages(context)
    }
}