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
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.bnyro.translate.R
import com.bnyro.translate.const.TranslationEngines
import com.bnyro.translate.ext.getText
import com.bnyro.translate.ext.hasText
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.ui.components.ButtonWithIcon
import com.bnyro.translate.ui.components.TranslationField
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.ui.models.UnsupportedLanguageException
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.TranslationEngine
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun TranslationComponent(
    modifier: Modifier,
    viewModel: TranslationModel,
    showLanguageSelector: Boolean = false,
    largeTextFields: Boolean = true,
    onTranslationError: (e: String, important: Boolean) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current
    var hasClip by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit, clipboard) {
        hasClip = clipboard.hasText()
    }

    LaunchedEffect(Unit) {
        viewModel.apiError.collect { apiError ->
            when (apiError) {
                is HttpException -> {
                    onTranslationError(
                        context.getString(R.string.translation_error_hint) + " (${apiError.message.orEmpty()})",
                        true
                    )
                }

                is UnsupportedLanguageException -> {
                    onTranslationError(
                        context.getString(
                            R.string.unsupported_language,
                            apiError.language.takeIf { !it.isAutoLanguage }?.name
                                ?: context.getString(R.string.auto)
                        ), false
                    )
                }

                null -> Unit

                else -> {
                    onTranslationError(apiError.localizedMessage.orEmpty(), false)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
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
                language = viewModel.sourceLanguage.copy(
                    code = viewModel.sourceLanguage.code.ifEmpty {
                        viewModel.translation.detectedLanguage.orEmpty()
                    }
                ),
                showLanguageSelector = showLanguageSelector,
                largeTextFields = largeTextFields,
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

            if (viewModel.translating) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                )
            } else {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp)
                        .size(70.dp, 1.dp)
                )
            }

            if (hasClip && viewModel.insertedText.isBlank()) {
                Row {
                    ButtonWithIcon(
                        text = stringResource(R.string.paste),
                        icon = Icons.Default.ContentPaste
                    ) {
                        coroutineScope.launch {
                            viewModel.insertedText = clipboard.getText().orEmpty()
                            viewModel.translateNow()
                        }
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

                        val manager =
                            ContextCompat.getSystemService(context, ClipboardManager::class.java)
                                ?: return@ButtonWithIcon

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

            if (!viewModel.simTranslationEnabled) {
                TranslationFieldForTranslation(
                    viewModel,
                    viewModel.translation,
                    showLanguageSelector = showLanguageSelector,
                    largeTextFields = largeTextFields
                )
            } else {
                viewModel.translatedTexts.filter { it.value.translatedText.isNotEmpty() }
                    .forEach { (engineName, translation) ->
                        val engine = TranslationEngines.engines.find { it.name == engineName }

                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            TranslationFieldForTranslation(
                                viewModel,
                                translation,
                                showLanguageSelector = showLanguageSelector,
                                largeTextFields = largeTextFields,
                                engine = engine
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(140.dp)
                        )
                    }
            }
        }

        if (scrollState.value > 100) {
            val fabModifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)

            if (largeTextFields) {
                FloatingActionButton(
                    modifier = fabModifier,
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowUpward, null)
                }
            } else {
                SmallFloatingActionButton(
                    modifier = fabModifier,
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

@Composable
private fun TranslationFieldForTranslation(
    viewModel: TranslationModel,
    translation: Translation,
    showLanguageSelector: Boolean,
    largeTextFields: Boolean,
    engine: TranslationEngine? = null,
) {
    TranslationField(
        translationModel = viewModel,
        isSourceField = false,
        largeTextFields = largeTextFields,
        text = translation.translatedText,
        language = viewModel.targetLanguage,
        showLanguageSelector = showLanguageSelector,
        translationEngine = engine,
        setLanguage = {
            if (it == viewModel.sourceLanguage) {
                viewModel.sourceLanguage = viewModel.targetLanguage
            }
            viewModel.targetLanguage = it
        },
        onEngineNameClick = {
            viewModel.setCurrentEngine(engine!!)
            viewModel.translation = translation
        }
    )
}
