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

package com.bnyro.translate.api.la

import com.bnyro.translate.api.la.obj.LaraTranslateRequest
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LaEngine : TranslationEngine(
    name = "LaraTranslate",
    defaultUrl = "https://webapi.laratranslate.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = ""
) {
    private lateinit var api: LaraTranslate

    override fun createOrRecreate() = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        var languagesHtml = api.getLanguages(LANGUAGES_LIST_URL)
            .data.content.body

        val languages = mutableListOf<Language>()

        while (languagesHtml.isNotBlank() && languagesHtml.contains("<li>")) {
            languagesHtml = languagesHtml.substringAfter("<li>")
            val languageInfo = languagesHtml.substringBefore("</li>")
            languagesHtml = languagesHtml.substringAfter("</li>")

            val (languageName, languageCode) = languageInfo.split(" - ")
            val lang = Language(code = languageCode.replace("`", ""), name = languageName)
            languages.add(lang)
        }

        return languages
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val requestBody =
            LaraTranslateRequest(q = query, source = sourceOrAuto(source), target = target)
        val translation = api.translate(requestBody)

        if (translation.status != 200) throw Exception("Received error response from LaraTranslate.")

        return Translation(
            translatedText = translation.content.translation,
            detectedLanguage = translation.content.sourceLanguage
        )
    }

    companion object {
        private const val LANGUAGES_LIST_URL =
            "https://developers.laratranslate.com/lara/api-next/v2/versions/1.0/guides/supported-languages"
    }
}