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

package com.bnyro.translate.api.kagi

import com.bnyro.translate.api.kagi.obj.KagiTranslationRequest
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Definition
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

/**
 * Kagi Translate API implementation
 *
 * Requires a session token for authentication
 */
class KagiEngine : TranslationEngine(
    name = "Kagi",
    defaultUrl = "https://translate.kagi.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.REQUIRED,
    autoLanguageCode = "auto",
    // don't touch these values, the code must be adapted when changing!
    supportedEngines = listOf(
        "Fastest model",
        "Fastest model with definitions",
        "Best model",
        "Best model with definitions",
    )
) {
    private lateinit var api: Kagi

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().map { kagiLang ->
            Language(
                code = kagiLang.language.lowercase(),
                name = kagiLang.name
            )
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val fetchDefinitions = getSelectedEngine().contains("definitions")
        val useBestModel = getSelectedEngine().contains("Best")

        val request = KagiTranslationRequest(
            text = query,
            sourceLang = sourceOrAuto(source),
            targetLang = target,
            skipDefinition = !fetchDefinitions,
            model = if (useBestModel) "best" else null
        )

        val response = api.translate(
            token = getApiKey(), // Session token as query parameter
            request = request
        )

        return Translation(
            translatedText = response.translation,
            detectedLanguage = response.detectedLanguage?.iso,
            definitions = response.definition?.let { def ->
                val primaryDefinition = def.primaryMeaning?.let { primary ->
                    Definition(
                        type = primary.partOfSpeech.joinToString(", "),
                        definition = primary.definition,
                        example = def.examples.firstOrNull()
                    )
                }

                val secondaryDefinitions = def.secondaryMeanings.map { secondary ->
                    Definition(
                        type = secondary.partOfSpeech.joinToString(", "),
                        definition = secondary.definition
                    )
                }

                (listOfNotNull(primaryDefinition) + secondaryDefinitions)
                    .takeIf { it.isNotEmpty() }
            },
            similar = response.definition?.let { def ->
                val synonyms =
                    def.primaryMeaning?.synonyms.orEmpty() + def.secondaryMeanings.flatMap { it.synonyms }

                synonyms.distinct().takeIf { it.isNotEmpty() }
            },
            examples = response.definition?.examples?.ifEmpty { null }
        )
    }
} 