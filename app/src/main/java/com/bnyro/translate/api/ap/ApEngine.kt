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

package com.bnyro.translate.api.ap

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.util.Locale

class ApEngine: TranslationEngine(
    name = "Apertium",
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = null,
    defaultUrl = "https://apertium.org",
    urlModifiable = false,
) {
    lateinit var api: Apertium

    override fun createOrRecreate() = apply {
        api = RetrofitHelper.createApi(this)
    }

    private val iso3ToIso2Map = Locale.getAvailableLocales().associate {
        it.isO3Language to it.language
    }

    private val iso2ToIso3Map = Locale.getAvailableLocales().associate {
        it.language to it.isO3Language
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().responseData
            .flatMap { listOf(it.sourceLanguage, it.targetLanguage) }
            .distinct()
            .map {
                val code = iso3ToIso2Map[it] ?: return@map null
                val locale = Locale.forLanguageTag(code)

                Language(
                    code = code,
                    name = locale.getDisplayName(locale)
                )
            }
            .filterNotNull()
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        // Apertium can only deal with 3 letter language codes, hence converting them here
        val source3 = iso2ToIso3Map[source] ?: throw IllegalArgumentException("unsupported languages $source")
        val target3 = iso2ToIso3Map[target] ?: throw IllegalArgumentException("unsupported language $target")

        return Translation(api.translate("$source3|$target3", query).responseData.translatedText)
    }
}
