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

package com.bnyro.translate.api.wm

import com.bnyro.translate.api.wm.obj.WmTranslationRequest
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.util.*

class WmEngine: TranslationEngine(
    name = "MinT",
    apiKeyState = ApiKeyState.DISABLED,
    supportsSimTranslation = true,
    autoLanguageCode = null,
    defaultUrl = "https://translate.wmcloud.org/",
    urlModifiable = true
) {
    lateinit var api: WikimediaMinT

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val json = api.getLanguages()
        return json.entries.map {
            val languageName = runCatching { Locale.forLanguageTag(it.key).displayName }.getOrNull()
            Language(it.key, languageName ?: it.key)
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(source, target, WmTranslationRequest(query))
        return Translation(response.translation)
    }
}