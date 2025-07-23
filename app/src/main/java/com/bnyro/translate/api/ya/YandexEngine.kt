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

import com.bnyro.translate.api.ya.obj.Yandex
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
        // to update, go to https://translate.yandex.com and search for TRANSLATOR_LANGS in the source code

        return JsonHelper.json.decodeFromString<Map<String, String>>(
            "{\"af\":\"Afrikaans\",\"sq\":\"Albanian\",\"am\":\"Amharic\",\"ar\":\"Arabic\",\"hy\":\"Armenian\",\"az\":\"Azerbaijani\",\"ba\":\"Bashkir\",\"eu\":\"Basque\",\"be\":\"Belarusian\",\"bn\":\"Bengali\",\"bs\":\"Bosnian\",\"bg\":\"Bulgarian\",\"my\":\"Burmese\",\"ca\":\"Catalan\",\"ceb\":\"Cebuano\",\"zh\":\"Chinese\",\"cv\":\"Chuvash\",\"hr\":\"Croatian\",\"cs\":\"Czech\",\"da\":\"Danish\",\"nl\":\"Dutch\",\"sjn\":\"Elvish (Sindarin)\",\"emj\":\"Emoji\",\"en\":\"English\",\"eo\":\"Esperanto\",\"et\":\"Estonian\",\"fi\":\"Finnish\",\"fr\":\"French\",\"gl\":\"Galician\",\"ka\":\"Georgian\",\"de\":\"German\",\"el\":\"Greek\",\"gu\":\"Gujarati\",\"ht\":\"Haitian\",\"he\":\"Hebrew\",\"mrj\":\"Hill Mari\",\"hi\":\"Hindi\",\"hu\":\"Hungarian\",\"is\":\"Icelandic\",\"id\":\"Indonesian\",\"ga\":\"Irish\",\"it\":\"Italian\",\"ja\":\"Japanese\",\"jv\":\"Javanese\",\"kn\":\"Kannada\",\"kk\":\"Kazakh\",\"kazlat\":\"Kazakh (Latin)\",\"km\":\"Khmer\",\"kv\":\"Komi\",\"ko\":\"Korean\",\"ky\":\"Kyrgyz\",\"lo\":\"Lao\",\"la\":\"Latin\",\"lv\":\"Latvian\",\"lt\":\"Lithuanian\",\"lb\":\"Luxembourgish\",\"mk\":\"Macedonian\",\"mg\":\"Malagasy\",\"ms\":\"Malay\",\"ml\":\"Malayalam\",\"mt\":\"Maltese\",\"mi\":\"Maori\",\"mr\":\"Marathi\",\"mhr\":\"Mari\",\"mn\":\"Mongolian\",\"ne\":\"Nepali\",\"no\":\"Norwegian\",\"os\":\"Ossetian\",\"pap\":\"Papiamento\",\"fa\":\"Persian\",\"pl\":\"Polish\",\"pt\":\"Portuguese\",\"pt-BR\":\"Portuguese (Brazilian)\",\"pa\":\"Punjabi\",\"ro\":\"Romanian\",\"ru\":\"Russian\",\"gd\":\"Scottish Gaelic\",\"sr\":\"Serbian\",\"sr-Latn\":\"Serbian (Latin)\",\"si\":\"Sinhalese\",\"sk\":\"Slovak\",\"sl\":\"Slovenian\",\"es\":\"Spanish\",\"su\":\"Sundanese\",\"sw\":\"Swahili\",\"sv\":\"Swedish\",\"tl\":\"Tagalog\",\"tg\":\"Tajik\",\"ta\":\"Tamil\",\"tt\":\"Tatar\",\"te\":\"Telugu\",\"th\":\"Thai\",\"tr\":\"Turkish\",\"tyv\":\"Tuvan\",\"udm\":\"Udmurt\",\"uk\":\"Ukrainian\",\"ur\":\"Urdu\",\"uz\":\"Uzbek\",\"uzbcyr\":\"Uzbek (Cyrillic)\",\"vi\":\"Vietnamese\",\"cy\":\"Welsh\",\"xh\":\"Xhosa\",\"sah\":\"Yakut\",\"yi\":\"Yiddish\",\"zu\":\"Zulu\"}"
        ).map { (key, value) -> Language(code = key, name = value) }
    }

    protected fun convertLanguageCode(languageCode: String): String {
        return when(languageCode){
            "zh-Hant", "zh-Hans" -> "zh"
            else -> languageCode
        }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val convertedSource = convertLanguageCode(source)
        val convertedTarget = convertLanguageCode(target)
        val lang = if (convertedSource.isEmpty()) convertedTarget else "$convertedSource-$convertedTarget"

        val uuid = UUID.randomUUID().toString().replace("-", "") + "-0-0"
        val response = api.translate(lang, query, "android", uuid)
        if (response.text.isEmpty()) throw Exception("Server didn't provide any translation.")
        return Translation(
            translatedText = response.text.first(),
            detectedLanguage = response.lang.split("-").last()
        )
    }
}