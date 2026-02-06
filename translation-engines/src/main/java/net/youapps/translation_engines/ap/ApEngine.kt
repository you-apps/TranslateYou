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

package net.youapps.translation_engines.ap

import java.util.Locale
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class ApEngine(settingsProvider: EngineSettingsProvider): TranslationEngine(settingsProvider) {
    override val name = "Apertium"
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = null
    override val defaultUrl = "https://apertium.org"
    override val urlModifiable = false

    lateinit var api: Apertium

    override fun createOrRecreate() = apply {
        api = RetrofitHelper.createApi(this)
    }

    private val iso3ToIso2Map: Map<String, String> by lazy {
        Locale.getISOLanguages().associate { iso2 ->
            Locale.forLanguageTag(iso2).isO3Language to iso2
        }
    }

    private val iso2ToIso3Map: Map<String, String> by lazy {
        Locale.getISOLanguages().associateWith { iso2 ->
            Locale.forLanguageTag(iso2).isO3Language
        }
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().responseData
            .flatMap { listOf(it.sourceLanguage, it.targetLanguage) }
            .distinct()
            .map {
                val code = iso3ToIso2Map[it] ?: return@map null
                val locale = Locale.forLanguageTag(code)

                Language(
                    code = code,
                    name = locale.getDisplayName(locale)
                )
            }
            .filterNotNull()
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        // Apertium can only deal with 3 letter language codes, hence converting them here
        val source3 = iso2ToIso3Map[source] ?: throw IllegalArgumentException("unsupported languages $source")
        val target3 = iso2ToIso3Map[target] ?: throw IllegalArgumentException("unsupported language $target")

        return Translation(api.translate("$source3|$target3", query).responseData.translatedText)
    }
}
