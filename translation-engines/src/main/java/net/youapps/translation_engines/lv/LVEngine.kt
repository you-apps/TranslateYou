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

package net.youapps.translation_engines.lv

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.Definition
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class LVEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "Lingva"
    override val defaultUrl = "https://lingva.ml"
    override val urlModifiable = true
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = "auto"
    override val supportsAudio = true

    private lateinit var api: LingvaTranslate
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val languages = api.getLanguages().languages
        // first language is auto language
        return languages.subList(1, languages.size)
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            sourceOrAuto(source),
            target,
            query.replace("/", "")
        )
        return Translation(
            translatedText = response.translation,
            detectedLanguage = response.info?.detectedSource,
            transliterations = listOfNotNull(response.info?.pronunciation?.query),
            examples = response.info?.examples,
            similar = response.info?.similar,
            definitions = response.info?.definitions
                ?.map {
                    Definition(
                        type = it.type,
                        definition = it.list.firstOrNull()?.definition,
                        example = it.list.firstOrNull()?.example,
                        synonym = it.list.firstOrNull()?.synonyms?.firstOrNull()
                    )
                }
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): ByteArray {
        return api.getAudio(lang, query).toByteArray()
    }
}
