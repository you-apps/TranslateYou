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

package com.bnyro.translate.ui.views

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bnyro.translate.R
import com.bnyro.translate.ui.components.ButtonWithIcon
import com.bnyro.translate.ui.components.TranslationField
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.Preferences
import kotlinx.coroutines.launch

@Composable
fun TranslationComponent(
    viewModel: TranslationModel,
    showLanguageSelector: Boolean = false
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboardManager.current
    var hasClip by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit, clipboard) {
        hasClip = clipboard.hasText() && !clipboard.getText()?.toString().isNullOrBlank()
    }

    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1.0f)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {
                TranslationField(
                    translationModel = viewModel,
                    isSourceField = true,
                    text = viewModel.insertedText,
                    viewModel.sourceLanguage,
                    showLanguageSelector = showLanguageSelector,
                    setLanguage = {
                        if (it == viewModel.targetLanguage) {
                            viewModel.targetLanguage = viewModel.sourceLanguage
                        }
                        viewModel.sourceLanguage = it
                    }
                ) {
                    viewModel.insertedText = it
                    hasClip = clipboard.hasText()
                    viewModel.enqueueTranslation()
                }

                val modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)

                if (viewModel.translating) {
                    LinearProgressIndicator(
                        modifier = modifier
                    )
                } else {
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = modifier
                            .size(70.dp, 1.dp)
                    )
                }

                if (hasClip && viewModel.insertedText.isBlank()) {
                    Row {
                        ButtonWithIcon(
                            text = stringResource(R.string.paste),
                            icon = Icons.Default.ContentPaste
                        ) {
                            viewModel.insertedText = clipboard.getText()?.toString().orEmpty()
                            viewModel.enqueueTranslation()
                        }

                        Spacer(
                            modifier = Modifier
                                .width(0.dp)
                        )

                        ButtonWithIcon(
                            text = stringResource(R.string.forget),
                            icon = Icons.Default.Clear
                        ) {
                            hasClip = false

                            val manager = ContextCompat.getSystemService(context, ClipboardManager::class.java) ?: return@ButtonWithIcon

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                manager.clearPrimaryClip()
                            } else {
                                manager.setPrimaryClip(ClipData(null))
                            }

                            viewModel.clearTranslation()
                        }
                    }
                } else if (
                    viewModel.insertedText.isNotBlank() &&
                    !Preferences.get(Preferences.translateAutomatically, true)
                ) {
                    ButtonWithIcon(
                        text = stringResource(R.string.translate),
                        icon = Icons.Default.Translate
                    ) {
                        viewModel.translateNow()
                    }
                }

                TranslationField(
                    translationModel = viewModel,
                    isSourceField = false,
                    text = viewModel.translation.translatedText,
                    language = viewModel.targetLanguage,
                    showLanguageSelector = showLanguageSelector,
                    setLanguage = {
                        if (it == viewModel.sourceLanguage) {
                            viewModel.sourceLanguage = viewModel.targetLanguage
                        }
                        viewModel.targetLanguage = it
                    }
                )
            }

            if (scrollState.value > 100) {
                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            }
        }
    }
}
