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

package net.youapps.translation_engines.wm

import java.util.*
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine
import net.youapps.translation_engines.wm.obj.WmTranslationRequest

class WmEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name = "MinT"
    override val apiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode = null
    override val defaultUrl = "https://translate.wmcloud.org/"
    override val urlModifiable = true

    lateinit var api: WikimediaMinT

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val json = api.getLanguages()
        return json.keys.map { languageCode ->
            val languageName = runCatching {
                Locale.forLanguageTag(languageCode).displayName
            }.getOrNull()

            Language(code = languageCode, name = languageName ?: languageCode)
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(source, target, WmTranslationRequest(query))
        return Translation(response.translation)
    }
}