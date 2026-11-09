/*
 * Copyright (c) 2025 You Apps
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

package net.youapps.translation_engines.deepl

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.deepl.obj.DeeplWebRequest

/**
 * DeepL translator using its unauthenticated browser endpoint.
 */
class DeeplBrowserEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "DeepL (Browser)"
    override val defaultUrl = "https://oneshot-free.www.deepl.com"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.DISABLED

    private lateinit var api: DeepL
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> =
        listOf(
            // List is on https://www.deepl.com/translator
            // Language code can be checked in URL after making a first translation
            Language("ar", "Arabic"),
            Language("bg", "Bulgarian"),
            Language("zh", "Chinese"),
            Language("cs", "Czech"),
            Language("da", "Danish"),
            Language("nl", "Dutch"),
            Language("en", "English"),
            Language("et", "Estonian"),
            Language("fi", "Finnish"),
            Language("fr", "French"),
            Language("de", "German"),
            Language("el", "Greek"),
            Language("hu", "Hungarian"),
            Language("id", "Indonesian"),
            Language("it", "Italian"),
            Language("ja", "Japanese"),
            Language("ko", "Korean"),
            Language("lv", "Latvian"),
            Language("lt", "Lithuanian"),
            Language("nb", "Norwegian (bokmål)"),
            Language("pl", "Polish"),
            Language("pt", "Portuguese"),
            Language("ro", "Romanian"),
            Language("ru", "Russian"),
            Language("sk", "Slovak"),
            Language("sl", "Slovenian"),
            Language("es", "Spanish"),
            Language("sv", "Swedish"),
            Language("tr", "Turkish"),
            Language("uk", "Ukrainian")
        )

    override suspend fun translate(query: String, source: String?, target: String): Translation {
        val body = DeeplWebRequest(
            text = listOf(query),
            targetLang = when (target.lowercase()) {
                "en" -> "en-US"
                "pt" -> "pt-BR"
                "zh" -> "zh-Hans"
                else -> target.lowercase()
            },
            sourceLang = when (source?.lowercase()) {
                "zh" -> "zh-Hans"
                else -> source?.lowercase()
            }
        )
        val result = api.webTranslate(body).translations.first()
        return Translation(
            translatedText = result.text,
            detectedLanguage = result.detectedSourceLanguage.lowercase()
        )
    }
}
