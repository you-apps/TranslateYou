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

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bnyro.translate.R
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.setText
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.SpeechHelper
import com.bnyro.translate.util.TranslationEngine
import kotlinx.coroutines.launch

@Composable
fun TranslationField(
    translationModel: TranslationModel,
    isSourceField: Boolean,
    text: String,
    language: Language,
    translationEngine: TranslationEngine? = null,
    setLanguage: (Language) -> Unit = {},
    showLanguageSelector: Boolean = false,
    onEngineNameClick: () -> Unit = {},
    onTextChange: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current

    val handler = remember {
        Handler(Looper.getMainLooper())
    }

    val charPref = remember {
        Preferences.get(Preferences.charCounterLimitKey, "")
    }

    AnimatedVisibility(
        visible = text.isNotEmpty(),
        enter = expandVertically(),
        exit = shrinkVertically(),
        label = "text actions fade"
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (translationEngine != null) {
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onEngineNameClick()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    text = translationEngine.name
                )
            }

            if (translationEngine != null && showLanguageSelector) {
                // visual separator
                Text("-")
            }

            if (showLanguageSelector) {
                LanguageSelector(
                    translationModel.availableLanguages,
                    language,
                    autoLanguageEnabled = translationModel.engine.autoLanguageCode != null && isSourceField,
                    viewModel = translationModel,
                    useElevatedButton = false
                ) {
                    setLanguage(it)
                    translationModel.translateNow()
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isSourceField && showLanguageSelector) {
                SwapLanguagesButton(translationModel)
            }

            var copyImageVector by remember {
                mutableStateOf(Icons.Default.ContentCopy)
            }
            StyledIconButton(
                imageVector = copyImageVector,
                onClick = {
                    scope.launch {
                        clipboard.setText(text)
                    }
                    copyImageVector = Icons.Default.DoneAll
                    handler.postDelayed({
                        copyImageVector = Icons.Default.ContentCopy
                    }, 2000)
                }
            )

            StyledIconButton(
                imageVector = Icons.Default.Share,
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(shareIntent)
                }
            )

            if (translationModel.engine.supportsAudio && language.code.isNotEmpty()) {
                StyledIconButton(
                    imageVector = Icons.AutoMirrored.Default.VolumeUp
                ) {
                    translationModel.playAudio(language.code, text)
                }
            } else if (SpeechHelper.ttsAvailable) {
                StyledIconButton(
                    imageVector = Icons.AutoMirrored.Default.VolumeUp
                ) {
                    SpeechHelper.speak(context, text, language.code)
                }
            }
        }
    }

    StyledTextField(
        text = text,
        placeholder = if (isSourceField) stringResource(R.string.enter_text) else null,
        readOnly = !isSourceField,
        textColor = if (
            isSourceField && charPref.isNotEmpty() && text.length >= charPref.toInt()
        ) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.typography.bodyMedium.color
        }
    ) {
        onTextChange(it)
    }
}