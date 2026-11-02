/*
 * Copyright (c) 2026 You Apps
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

package net.youapps.translation_engines.bi

import java.util.*
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class BingEngine(settingsProvider: EngineSettingsProvider) : TranslationEngine(settingsProvider) {
    override val name: String = "Bing"
    override val defaultUrl: String = "https://www.bing.com"
    override val urlModifiable: Boolean = false
    override val apiKeyState: ApiKeyState = ApiKeyState.DISABLED
    override val autoLanguageCode: String = "auto-detect"

    private lateinit var api: Bing

    private val abuseParamsRegex =
        Regex("""params_AbusePreventionHelper\s*=\s*\[(\d+),"(\w+)",(\d+)]""")
    private val iidRegex = Regex("""data-iid="(.*?)"""")
    private val igRegex = Regex("""IG:"([A-F0-9]+)"""")

    private val languagesRegex = Regex("""languageMapping\s*=\s*\[(.*?)]""")

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        val translatorPage = api.getTranslatorPage().string()
        val languageCodes = languagesRegex.find(translatorPage)!!.groupValues[1]
            .split(",")
            .map { it.trim('"') }

        return languageCodes.mapNotNull { code ->
            val languageName = Locale(code).displayLanguage
            languageName.takeIf { it != code }?.let { Language(code = code, name = it) }
        }
    }

    override suspend fun translate(
        query: String,
        source: String,
        target: String
    ): Translation {
        val translatorPage = api.getTranslatorPage().string()
        val params = abuseParamsRegex.find(translatorPage)!!

        val key = params.groupValues[1]
        val token = params.groupValues[2]

        val iid = iidRegex.find(translatorPage)!!.groupValues[1]
        val ig = igRegex.find(translatorPage)!!.groupValues[1]

        val response = api.translate(
            ig = ig,
            iid = iid,
            key = key,
            token = token,
            fromLang = sourceOrAuto(source),
            toLang = target,
            text = query
        )[0]

        return Translation(
            translatedText = response.translations[0].text,
            detectedLanguage = response.detectedLanguage.language
        )
    }
}