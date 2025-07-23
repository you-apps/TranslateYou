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

package com.bnyro.translate.api.deepl

import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationRequest
import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationRequestParams
import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationRequestParamsLang
import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationRequestParamsLangPreference
import com.bnyro.translate.api.deepl.obj.DeeplWebTranslationRequestParamsText
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.text.ifEmpty
import kotlinx.serialization.json.Json

/**
 * DeepL translator, emulating a browser
 *
 * Uses the API if an API key is provided
 * Fallbacks to emulating a browser (less reliable method) otherwise
 */
class DeeplBrowserEngine : TranslationEngine(
    name = "DeepL (Browser)",
    defaultUrl = "https://www2.deepl.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = null
) {
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

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val id = (floor(Math.random().times(99999)) + 100000).roundToInt().times(1000)
        val body = Json.encodeToString(
            DeeplWebTranslationRequest(
                jsonrpc = "2.0",
                method = "LMT_handle_texts",
                params = DeeplWebTranslationRequestParams(
                    texts = listOf(
                        DeeplWebTranslationRequestParamsText(
                            text = query,
                        )
                    ),
                    splitting = "newlines",
                    lang = DeeplWebTranslationRequestParamsLang(
                        targetLang = target.uppercase(),
                        sourceLangUserSelected = sourceOrAuto(source.uppercase()).ifEmpty { "auto" },
                        preference = DeeplWebTranslationRequestParamsLangPreference(
                            weight = emptyMap()
                        )
                    ),
                    commonJobParams = emptyMap(),
                    timestamp = System.currentTimeMillis()
                ),
                id = id
            )
        ).replace(
            "\"method\":\"",
            // The random ID determines the spacing to use, do NOT change it
            // This is how the client side of the web service works and the server-side
            // expects the same, otherwise you will get soft-banned
            if ((id + 3) % 13 == 0 || (id + 5) % 29 == 0) {
                "\"method\" : \""
            } else "\"method\": \""
        )
        val webResponse = api.webTranslate(body)

        return Translation(
            translatedText = webResponse.result.texts.firstOrNull()?.text ?: "",
            detectedLanguage = webResponse.result.lang
        )
    }

    companion object {
        const val WEB_CHROME_EXTENSION_VER = "1.49.0"
        const val WEB_CHROME_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36"
    }
}
