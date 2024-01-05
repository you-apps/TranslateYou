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

package com.bnyro.translate.api.or

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine
import java.util.*

class OneRingEngine: TranslationEngine(
    name = "OneRing",
    apiKeyState = ApiKeyState.OPTIONAL,
    urlModifiable = true,
    defaultUrl = "https://your.instance.domain",
    autoLanguageCode = "",
    supportedEngines = listOf(
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
) {
   private lateinit var api: OneRing

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return Locale.getAvailableLocales().map {
            Language(it.isO3Language, it.getDisplayName(Locale.getDefault()))
        }
            .distinctBy { it.code }
            .sortedBy { it.name }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            text = query,
            from = source,
            to = target,
            apiKey = getApiKey(),
            plugin = getSelectedEngine()
        )

        return Translation(response.result)
    }
}