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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.bnyro.translate.ui.models.TranslationModel
import com.bnyro.translate.util.SpeechHelper

@Composable
fun TTSButton(
    viewModel: TranslationModel,
    languageCode: String,
    text: String
) {
    val context = LocalContext.current

    AnimatedVisibility(visible = text.isNotEmpty(), label = "tts") {
        if (viewModel.engine.supportsAudio) {
            StyledIconButton(
                imageVector = Icons.Default.VolumeUp
            ) {
                viewModel.playAudio(languageCode, text)
            }
        } else if (SpeechHelper.ttsAvailable) {
            StyledIconButton(
                imageVector = Icons.Default.VolumeUp
            ) {
                SpeechHelper.speak(context, text, languageCode)
            }
        }
    }
}
