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

package net.youapps.translation_engines.mh

import kotlin.collections.orEmpty
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.Definition
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class MhEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "Mozhi"
    override val defaultUrl = "https://mozhi.aryak.me/"
    override val urlModifiable = true
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = "auto"

    override val supportsAudio = true

    lateinit var api: Mozhi
    private val transliterationFailedRegex = Regex("Direction \'\\w{2}\' is not supported")
    private val bracketRegex = Regex("[<>]")

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages(getSelectedModel())
            .map { Language(it.id, it.name) }
    }

    override val supportedModels: List<String> = listOf(
        "google",
        "libre",
        "reverso",
        "deepl",
        "duckduckgo",
        "mymemory",
        "watson",
        "yandex"
    )

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            engine = getSelectedModel(),
            source = sourceOrAuto(source.take(2)),
            query = query,
            target = target.take(2),
        )
        return Translation(
            translatedText = response.translatedText,
            transliterations = listOf(
                response.sourceTransliteration,
                response.targetTransliteration
            ).filter {
                it.isNotBlank() && !it.matches(transliterationFailedRegex)
            },
            detectedLanguage = response.detectedLanguage.takeIf { !it.isNullOrBlank() },
            definitions = response.wordChoices.orEmpty().map { definition ->
                Definition(
                    definition = definition.definition.takeIf { it.isNotBlank() },
                    example = definition.example.takeIf { it.isNotBlank() }
                )
            },
            similar = response.targetSynonyms,
            examples = response.wordChoices.orEmpty()
                .flatMap { it.examplesSource.orEmpty() + it.examplesTarget.orEmpty() }
                .map { it.replace(bracketRegex, "") }
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): ByteArray? {
        return api.getAudioFile(lang = lang, text = query).body()?.bytes()
    }
}
