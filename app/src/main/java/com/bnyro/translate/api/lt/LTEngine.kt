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

package com.bnyro.translate.api.lt

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LTEngine : TranslationEngine(
    name = "LibreTranslate",
    defaultUrl = "https://translate.terraprint.co/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.OPTIONAL,
    autoLanguageCode = "auto"
) {

    private lateinit var api: LibreTranslate
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().map {
            Language(it.code!!, it.name!!)
        }
    }

    override suspend fun translate(
        query: String,
        source: String,
        target: String
    ): Translation {
        val response = api.translate(
            query,
            sourceOrAuto(source),
            target,
            getApiKey()
        )
        return Translation(
            translatedText = response.translatedText,
            detectedLanguage = response.detectedLanguage?.language
        )
    }
}
