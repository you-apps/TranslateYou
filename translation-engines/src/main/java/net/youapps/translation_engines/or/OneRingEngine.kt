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

package net.youapps.translation_engines.or

import java.util.*
import net.youapps.translation_engines.ApiKeyState
import net.youapps.translation_engines.EngineSettingsProvider
import net.youapps.translation_engines.Language
import net.youapps.translation_engines.RetrofitHelper
import net.youapps.translation_engines.Translation
import net.youapps.translation_engines.TranslationEngine

class OneRingEngine(settingsProvider: EngineSettingsProvider) :
    TranslationEngine(settingsProvider) {
    override val name = "OneRing"
    override val apiKeyState = ApiKeyState.OPTIONAL
    override val urlModifiable = true
    override val defaultUrl = "https://your.instance.domain"
    override val autoLanguageCode = ""
    override val supportedModels = listOf(
        "no_translate2",
        "no_translate",
        "fb_nllb_ctranslate2",
        "bloomz",
        "vsegpt_chat",
        "fb_nllb_translate",
        "opus_mt",
        "google_translate",
        "deepl",
        "deepl_translate",
        "use_mid_lang",
        "fb_mbart50",
        "openai_chat",
        "libre_translate",
        "koboldapi_translate",
        "lingvanex",
        "multi_sources"
    )

    private lateinit var api: OneRing

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return Locale.getAvailableLocales().map {
            Language(it.language, it.getDisplayName(Locale.getDefault()))
        }
            .distinctBy { it.code }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            text = query,
            from = source,
            to = target,
            apiKey = getApiKey(),
            plugin = getSelectedModel() ?: throw IllegalArgumentException("No model selected")
        )

        return Translation(response.result)
    }
}