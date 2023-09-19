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

package com.bnyro.translate.api.reverso

import com.bnyro.translate.api.reverso.obj.ReversoRequestBody
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.concatenate
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class ReversoEngine : TranslationEngine(
    name = "Reverso",
    defaultUrl = "https://api.reverso.net/",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportsSimTranslation = false
) {
    lateinit var api: Reverso

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return listOf(
            Language("ara", "Arabic"),
            Language("chi", "Chinese (Simplified)"),
            Language("dut", "Dutch"),
            Language("eng", "English"),
            Language("fra", "French"),
            Language("ger", "German"),
            Language("heb", "Hebrew"),
            Language("ita", "Italian"),
            Language("jpn", "Japanese"),
            Language("kor", "Korean"),
            Language("pol", "Polish"),
            Language("por", "Portuguese"),
            Language("rum", "Romanian"),
            Language("rus", "Russian"),
            Language("spa", "Spanish"),
            Language("swe", "Swedish"),
            Language("tur", "Turkish"),
            Language("ukr", "Ukrainian")
        )
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            requestBody = ReversoRequestBody(
                from = sourceOrAuto(source),
                to = target,
                input = query
            )
        )

        return Translation(
            translatedText = response.translation.firstOrNull() ?: "",
            detectedLanguage = response.languageDetection?.detectedLanguage,
            similar = response.contextResults?.results
                ?.filter { it.translation != null }
                ?.map { it.translation!! },
            examples = response.contextResults?.results?.let { result ->
                concatenate(
                    result.map { it.sourceExamples }.flatten(),
                    result.map { it.targetExamples }.flatten()
                )
            }
        )
    }
}
