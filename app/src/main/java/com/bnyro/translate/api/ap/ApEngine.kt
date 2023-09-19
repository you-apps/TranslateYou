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

    override suspend fun getLanguages(): List<Language> {
        return listOf(
            "af" to "Afrikaans",
            "an" to "Aragonese",
            "ar" to "Arabic",
            "as" to "Assamese",
            "ast" to "Asturian",
            "av" to "Avaric",
            "az" to "Azerbaijani",
            "ba" to "Bashkir",
            "bak" to "Bashqort",
            "be" to "Belarusian",
            "bg" to "Bulgarian",
            "bn" to "Bengali",
            "br" to "Breton",
            "bs" to "Bosnian",
            "ca" to "Catalan",
            "cat_valencia" to "Valencian",
            "crh" to "Crimean Tatar",
            "cs" to "Czech",
            "cv" to "Chuvash",
            "cy" to "Welsh",
            "da" to "Danish",
            "de" to "German",
            "el" to "Greek",
            "en" to "English",
            "eo" to "Esperanto",
            "es" to "Spanish",
            "et" to "Estonian",
            "eu" to "Basque",
            "fa" to "Persian",
            "fi" to "Finnish",
            "fo" to "Faroese",
            "fr" to "French",
            "frp" to "Arpitan",
            "ga" to "Irish",
            "gd" to "Scottish Gaelic",
            "gl" to "Galician",
            "gv" to "Manx",
            "he" to "Hebrew",
            "hi" to "Hindi",
            "hr" to "Croatian",
            "hu" to "Hungarian",
            "hy" to "Armenian",
            "ia" to "Interlingua",
            "id" to "Indonesian",
            "is" to "Icelandic",
            "it" to "Italian",
            "kaa" to "Karakalpak",
            "kk" to "Kazakh",
            "ko" to "Korean",
            "kum" to "Kumyk",
            "ky" to "Kyrgyz",
            "lg" to "Ganda",
            "lo" to "Lao",
            "lt" to "Lithuanian",
            "lv" to "Latvian",
            "mfe" to "Morisyen",
            "mk" to "Macedonian",
            "ml" to "Malayalam",
            "mr" to "Marathi",
            "mrj" to "Western Mari",
            "ms" to "Malay (macrolanguage)",
            "mt" to "Maltese",
            "myv" to "Erzya",
            "nb" to "Norwegian Bokmål",
            "ne" to "Nepali",
            "nl" to "Dutch",
            "nn" to "Norwegian Nynorsk",
            "nno_e" to "East Norwegian, vi→vi",
            "nog" to "Noghay",
            "oc" to "Occitan",
            "os" to "Ossetian",
            "pa" to "Punjabi",
            "pes" to "Iranian Persian",
            "pl" to "Polish",
            "por_BR" to "Brazilian Portuguese",
            "por_PTpre1990" to "European Port. (trad. spelling)",
            "pt" to "Portuguese",
            "rm" to "Romansh",
            "rn" to "Rundi",
            "ro" to "Romanian",
            "ru" to "Russian",
            "sah" to "Sakha",
            "sat" to "Santali",
            "sc" to "Sardinian",
            "se" to "Northern Sami",
            "sh" to "Serbo-Croatian",
            "si" to "Sinhala",
            "sk" to "Slovak",
            "sl" to "Slovenian",
            "sq" to "Albanian",
            "sr" to "Serbian",
            "srd" to "Sardinian",
            "sv" to "Swedish",
            "sw" to "Swahili (macrolanguage)",
            "szl" to "Silesian",
            "te" to "Telugu",
            "tg" to "Tajik",
            "th" to "Thai",
            "tr" to "Turkish",
            "tt" to "Tatar",
            "tyv" to "Tuvan",
            "ug" to "Uyghur",
            "uk" to "Ukrainian",
            "ur" to "Urdu",
            "uz" to "Uzbek",
            "vi" to "Vietnamese",
            "xh" to "Xhosa",
            "zh" to "Chinese",
            "zlm" to "Malay",
            "zu" to "Zulu"
        ).map { Language(it.first, it.second) }.sortedBy { it.name }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        return Translation(api.translate("$source|$target", query).responseData.translatedText)
    }
}
