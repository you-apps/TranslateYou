/*
 * Copyright (c) 2024 You Apps
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

package com.bnyro.translate.api.po

import com.bnyro.translate.api.po.obj.PonsData
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.lang.IllegalArgumentException
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class PonsEngine : TranslationEngine(
    name = "Pons",
    defaultUrl = "https://api.pons.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = ""
) {
    private lateinit var api: Pons

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages()["languages"]?.jsonObject?.map {
            val langName = it.value.jsonObject["display"]?.jsonPrimitive?.content ?: throw IllegalArgumentException()
            Language(it.key, langName)
        }?.sortedBy { it.name } ?: throw IllegalArgumentException()
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val requestBody = PonsData(source.takeIf { it.isNotEmpty() }, target, query)
        val response = api.translate(body = requestBody)

        return Translation(response.text, response.sourceLanguage)
    }
}