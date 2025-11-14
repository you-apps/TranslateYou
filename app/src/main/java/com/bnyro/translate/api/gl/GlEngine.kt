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

package com.bnyro.translate.api.gl

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class GlEngine : TranslationEngine(
    apiKeyState = ApiKeyState.DISABLED,
    name = "Glosbe",
    autoLanguageCode = null,
    defaultUrl = "https://translator-api.glosbe.com/",
    urlModifiable = false,
) {
    lateinit var api: Glosbe

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().popularLanguages.map {
            Language(it.code, it.name)
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        return Translation(api.translate(source, target, query).translation)
    }
}
