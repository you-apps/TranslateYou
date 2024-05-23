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

package com.bnyro.translate.api.st

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.jsonPrimitive

class STEngine : TranslationEngine(
    name = "SimplyTranslate",
    defaultUrl = "https://simplytranslate.org/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto",
    supportedEngines = listOf("google", "libre", "reverso", "iciba"),
    supportsAudio = true
) {
    lateinit var api: SimplyTranslate

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages(getSelectedEngine()).map {
            Language(it.key, it.value.jsonPrimitive.content)
        }.sortedBy { it.code }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            engine = getSelectedEngine(),
            source = sourceOrAuto(source),
            query = query,
            target = target
        )
        return Translation(
            translatedText = response.translatedText,
            detectedLanguage = response.sourceLanguage,
            transliterations = listOfNotNull(response.pronunciation?.takeIf { it.isNotBlank() })
        )
    }

    override suspend fun getAudioFile(lang: String, query: String): File? {
        val audioBytes = api.getAudioFile(
            lang = lang,
            text = query,
            engine = getSelectedEngine()
        ).body()?.bytes() ?: return null

        return withContext(Dispatchers.IO) {
            File.createTempFile("audio", ".mp3")
        }.apply {
            writeBytes(audioBytes)
        }
    }
}
