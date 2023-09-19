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

import android.accounts.NetworkErrorException
import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class STEngine : TranslationEngine(
    name = "SimplyTranslate",
    defaultUrl = "https://simplytranslate.org/",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED,
    autoLanguageCode = "auto"
) {
    lateinit var api: SimplyTranslate
    val selEnginePrefKey = this.name + "selectedEngine"

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val body = api.getLanguages(getSelectedEngine())

        // val body = api.getLanguages().execute().body()
        val languages = mutableListOf<Language>()
        body.split("\n").let {
            for (index in 0..(it.size.toDouble() / 2 - 1).toInt()) {
                languages.add(
                    Language(name = it[index * 2], code = it[index * 2 + 1])
                )
            }
        }

        if (languages.isEmpty()) throw NetworkErrorException("Network Error")
        return languages
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            engine = getSelectedEngine(),
            source = sourceOrAuto(source),
            query = query,
            target = target
        )
        return Translation(
            translatedText = response.translated_text,
            detectedLanguage = response.source_language
        )
    }

    private fun getSelectedEngine(): String? {
        Preferences.get(selEnginePrefKey, "all").let {
            return when (it) {
                "all" -> null
                else -> it
            }
        }
    }
}
