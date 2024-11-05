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
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.parcelable
import com.bnyro.translate.ui.components.AppHeader
import com.bnyro.translate.ui.components.DialogButton
import com.bnyro.translate.ui.views.TranslationComponent
import com.bnyro.translate.util.ImageHelper

class ShareActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleIntentData()

        showContent {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp

            LaunchedEffect(Unit) {
                translationModel.refresh(this@ShareActivity)
            }

            AlertDialog(modifier = Modifier
                .heightIn(
                    max = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) screenHeight * 2 / 3 else Dp.Unspecified
                )
                .padding(horizontal = 10.dp), properties = DialogProperties(
                dismissOnClickOutside = false, usePlatformDefaultWidth = false
            ), onDismissRequest = { finish() }, confirmButton = {
                DialogButton(
                    text = stringResource(R.string.okay)
                ) {
                    finish()
                }
            }, dismissButton = {
                DialogButton(text = stringResource(R.string.clear)) {
                    translationModel.clearTranslation()
                }
            }, title = {
                AppHeader()
            }, text = {
                TranslationComponent(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = translationModel,
                    showLanguageSelector = true
                )
            })
        }

        setFinishOnTouchOutside(false)
    }

    @SuppressLint("InlinedApi")
    private fun getIntentText(): String? {
        return intent.getCharSequenceExtra(Intent.EXTRA_TEXT)?.toString()
            ?: intent.takeIf { Build.VERSION.SDK_INT > Build.VERSION_CODES.M }
                ?.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)?.toString()
            ?: intent.getCharSequenceExtra(Intent.ACTION_SEND)?.toString()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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
}