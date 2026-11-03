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

package net.youapps.translation_engines.na

import java.util.Locale
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class PapagoEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name: String = "Papago"
    override val defaultUrl: String = "https://papago.naver.com"
    override val urlModifiable: Boolean = false
    override val apiKeyState: ApiKeyState = ApiKeyState.DISABLED

    private val languageRegex = Regex("""\\"LANG_(\w{2})\\"""")

    private lateinit var api: Papago

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val response = api.mainPage().string()

        return languageRegex.findAll(response).map {
            val code = it.groupValues[1].lowercase()
            val languageName = Locale(code).displayLanguage

            Language(code = code, name = languageName)
        }.toList()
    }

    override suspend fun translate(
        query: String,
        source: String?,
        target: String
    ): Translation {
        val response = api.translate(source ?: "auto", target, query)

        return Translation(
            translatedText = response.translatedText,
            detectedLanguage = response.srcLangType,
        )
    }
}