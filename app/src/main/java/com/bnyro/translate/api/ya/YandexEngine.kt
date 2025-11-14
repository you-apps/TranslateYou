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

package com.bnyro.translate.api.ya

import com.bnyro.translate.api.ya.Yandex
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.JsonHelper
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.util.UUID

class YandexEngine : TranslationEngine(
    name = "Yandex",
    defaultUrl = "https://translate.yandex.net",
    urlModifiable = false,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = ""
) {
    private lateinit var api: Yandex

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val langStr = api.getMainPage()
            .substringAfter("TRANSLATOR_LANGS:")
            .substringBefore("\n")
            .trimEnd(',')

        return JsonHelper.json.decodeFromString<Map<String, String>>(langStr)
            .map { (key, value) -> Language(code = key, name = value) }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val lang = if (source.isEmpty()) target else "$source-$target"

        val uuid = UUID.randomUUID().toString().replace("-", "") + "-0-0"
        val response = api.translate(lang, query, "android", uuid)
        if (response.text.isEmpty()) throw Exception("Server didn't provide any translation.")
        return Translation(
            translatedText = response.text.first(),
            detectedLanguage = response.lang.split("-").last()
        )
    }
}