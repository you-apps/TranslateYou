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

package net.youapps.translation_engines.la

import net.youapps.translation_engines.la.obj.LaraTranslateRequest
import kotlin.text.get
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class LaEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "LaraTranslate"
    override val defaultUrl = "https://webapi.laratranslate.com"
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = ""

    private lateinit var api: LaraTranslate
    private val langRegex = Regex("""<li>(?<name>.*?) - `(?<code>.*?)`</li>""")

    override fun createOrRecreate() = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val languagesHtml = api.getLanguages(LANGUAGES_LIST_URL)
            .data.content.body

        return langRegex.findAll(languagesHtml).map {
            Language(
                code = it.groups["code"]!!.value.substringBefore("-"), // remove country info
                name = it.groups["name"]!!.value.substringBefore("(").trim()
            )
        }
            .toList()
            .distinct()
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
            "https://developers.laratranslate.com/lara/api-next/v2/versions/1.5/guides/supported-languages"
    }
}