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

package net.youapps.translation_engines.gl

import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class GlEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val apiKeyState = ApiKeyState.DISABLED
    override val name = "Glosbe"
    override val autoLanguageCode = null
    override val defaultUrl = "https://translator-api.glosbe.com/"
    override val  urlModifiable = false

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
