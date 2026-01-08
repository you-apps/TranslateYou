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

package net.youapps.translation_engines.deepl

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

/**
 * DeepL translator
 *
 * Uses the API if an API key is provided
 * Fallbacks to emulating a browser (less reliable method) otherwise
 */
abstract class DeeplAuthenticatedEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val urlModifiable = false
    override val apiKeyState = ApiKeyState.REQUIRED
    override val autoLanguageCode = ""

    private lateinit var api: DeepL
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    private val apiKeyString get() = "DeepL-Auth-Key " + getApiKey()

    override suspend fun getLanguages(): List<Language> =
            api.getLanguages(
                apiKeyString
            ).map {
                Language(
                    code = it.language.lowercase(),
                    name = it.name
                )
            }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            apiKeyString,
            sourceOrAuto(source.uppercase()),
            target.uppercase(),
            query
        )

        return Translation(
            translatedText = response.translations.first().text,
            detectedLanguage = response.translations.first().detectedSourceLanguage
        )
    }
}

class DeeplAuthenticatedFreeApiEngine(settingsProvider: EngineSettingsProvider) : DeeplAuthenticatedEngine(settingsProvider) {
    override val name: String = "DeepL (Authenticated, free API)"
    override val defaultUrl: String = "https://api-free.deepl.com"
}

class DeeplAuthenticatedPaidApiEngine(settingsProvider: EngineSettingsProvider) : DeeplAuthenticatedEngine(settingsProvider) {
    override val name: String = "DeepL (Authenticated, paid API)"
    override val defaultUrl: String = "https://api.deepl.com"
}