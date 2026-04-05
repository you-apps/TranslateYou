/*
 * Copyright (c) 2026 You Apps
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

package com.bnyro.translate.engine

import android.content.Context
import com.google.ai.edge.aicore.GenerativeModel
import com.google.ai.edge.aicore.GenerativeModelAvailability
import com.google.ai.edge.aicore.generationConfig
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class GeminiNanoEngine(
    private val context: Context,
    settingsProvider: EngineSettingsProvider
) : TranslationEngine(settingsProvider) {
    override val name = "Gemini Nano (On-Device)"
    override val defaultUrl = ""
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = "auto"
    override val supportsAudio = false

    private var model: GenerativeModel? = null

    override fun createOrRecreate(): TranslationEngine = apply {
        val config = generationConfig {
            this.context = this@GeminiNanoEngine.context
            temperature = 0.2f
            topK = 16
            maxOutputTokens = 512
        }
        model = GenerativeModel(generationConfig = config)
    }

    override suspend fun getLanguages(): List<Language> = SUPPORTED_LANGUAGES

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val currentModel = model
            ?: throw IllegalStateException("Gemini Nano model is not initialized")

        val availability = currentModel.checkAvailability()
        if (availability != GenerativeModelAvailability.AVAILABLE) {
            throw IllegalStateException(
                "Gemini Nano is not available on this device (status: $availability). " +
                    "This feature requires a device with AICore support (e.g., Pixel 8 or newer with Android 14+)."
            )
        }

        val sourceName = if (source.isEmpty() || source == "auto") {
            "the detected source language"
        } else {
            LANGUAGE_NAMES[source] ?: source
        }
        val targetName = LANGUAGE_NAMES[target] ?: target

        val prompt = "Translate the following text from $sourceName to $targetName. " +
            "Output only the translated text with no additional commentary or explanation:\n\n$query"

        val response = currentModel.generateContent(prompt)
        return Translation(translatedText = response.text?.trim() ?: "")
    }

    companion object {
        private val SUPPORTED_LANGUAGES = listOf(
            Language("ar", "Arabic"),
            Language("bn", "Bengali"),
            Language("bg", "Bulgarian"),
            Language("zh", "Chinese (Simplified)"),
            Language("zh-TW", "Chinese (Traditional)"),
            Language("hr", "Croatian"),
            Language("cs", "Czech"),
            Language("da", "Danish"),
            Language("nl", "Dutch"),
            Language("en", "English"),
            Language("et", "Estonian"),
            Language("fi", "Finnish"),
            Language("fr", "French"),
            Language("de", "German"),
            Language("el", "Greek"),
            Language("gu", "Gujarati"),
            Language("he", "Hebrew"),
            Language("hi", "Hindi"),
            Language("hu", "Hungarian"),
            Language("id", "Indonesian"),
            Language("it", "Italian"),
            Language("ja", "Japanese"),
            
            Language("ko", "Korean"),
            Language("lv", "Latvian"),
            Language("lt", "Lithuanian"),
            Language("ms", "Malay"),
            Language("ml", "Malayalam"),
            Language("mr", "Marathi"),
            Language("no", "Norwegian"),
            Language("fa", "Persian"),
            Language("pl", "Polish"),
            Language("pt", "Portuguese"),
            Language("ro", "Romanian"),
            Language("ru", "Russian"),
            Language("sr", "Serbian"),
            Language("sk", "Slovak"),
            Language("sl", "Slovenian"),
            Language("es", "Spanish"),
            Language("sw", "Swahili"),
            Language("sv", "Swedish"),
            Language("ta", "Tamil"),
            Language("te", "Telugu"),
            Language("th", "Thai"),
            Language("tr", "Turkish"),
            Language("uk", "Ukrainian"),
            Language("ur", "Urdu"),
            Language("vi", "Vietnamese"),
        )

        private val LANGUAGE_NAMES = SUPPORTED_LANGUAGES.associate { it.code to it.name }
    }
}
