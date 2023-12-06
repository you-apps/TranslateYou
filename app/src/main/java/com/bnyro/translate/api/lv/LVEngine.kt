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

package com.bnyro.translate.api.lv

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.ext.expOrNull
import com.bnyro.translate.obj.Definition
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LVEngine : TranslationEngine(
    name = "Lingva",
    defaultUrl = "https://lingva.lunar.icu",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportsAudio = true
) {

    private lateinit var api: LingvaTranslate
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().languages.toMutableList().apply {
            removeAt(0)
        }
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
                        definition = expOrNull { it.list.first().definition },
                        example = expOrNull { it.list.first().example },
                        synonym = expOrNull { it.list.first().synonyms.first() }
                    )
                }
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): File? {
        val byteArray = api.getAudio(lang, query).toByteArray()
        if (byteArray.isEmpty()) return null

        return File.createTempFile("audio", ".mp3").apply {
            writeBytes(byteArray)
        }
    }
}
