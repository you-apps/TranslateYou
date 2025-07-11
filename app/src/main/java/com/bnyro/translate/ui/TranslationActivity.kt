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

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.hexToColor
import com.bnyro.translate.ext.parcelable
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.theme.TranslateYouTheme
import com.bnyro.translate.util.ImageHelper
import com.bnyro.translate.util.LocaleHelper
import com.bnyro.translate.util.Preferences

open class TranslationActivity: ComponentActivity() {
    lateinit var translationModel: TranslationModel
    var themeMode by mutableStateOf(Preferences.getThemeMode())
    var accentColor by mutableStateOf(Preferences.getAccentColor())

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.updateLanguage(this)

        translationModel = ViewModelProvider(this)[TranslationModel::class.java]
        handleIntentData()

        super.onCreate(savedInstanceState)
    }

    fun showContent(content: @Composable () -> Unit) {
        setContent {
            TranslateYouTheme(themeMode, accentColor?.hexToColor()) {
                content()
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun getIntentText(): String? {
        return intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
            ?: intent.takeIf { Build.VERSION.SDK_INT > Build.VERSION_CODES.M }
                ?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
            ?: intent.getCharSequenceExtra(Intent.ACTION_SEND)?.toString()
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        this.intent = intent
        handleIntentData()
    }

    private fun handleIntentData() {
        getIntentText()?.let {
            translationModel.insertedText = it
            translationModel.translateNow(this)
        }
        // open links from Google Translate
        if (intent.data?.host == "translate.google.com") {
            val source = intent.data?.getQueryParameter("sl").orEmpty()
            val target = intent.data?.getQueryParameter("tl").orEmpty()
            translationModel.sourceLanguage = Language(source, source)
            translationModel.targetLanguage = Language(target, target)
            translationModel.insertedText = intent.data?.getQueryParameter("text").orEmpty()
            translationModel.translateNow(this)
        }
        if (intent.type?.startsWith("image/") != true) return

        (intent.parcelable<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            ImageHelper.getImage(this, it)?.let { bm ->
                translationModel.processImage(this, bm)
            }
        }
    }

    override fun onStop() {
        translationModel.saveSelectedLanguages()
        super.onStop()
    }
}