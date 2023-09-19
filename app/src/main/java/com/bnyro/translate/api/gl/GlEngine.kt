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

/* Python script to generate a list of languages from https://glosbe.com/all-languages
from pyquery import PyQuery

with open("languages.html") as f:
    text = f.read()

pq = PyQuery(text)
content = pq("li")

languages = []
for item in content.items():
    name = item("a").text()
    code = item("span:first-child").text()
    languages += [{"name": name, "code": code}]

with open("out.txt", "w") as f:
    f.write("listOf(")
    for language in languages:
        f.write(f"\"{language['code']}\" to \"{language['name']}\",")
    f.write(")")
 */

class GlEngine : TranslationEngine(
    apiKeyState = ApiKeyState.DISABLED,
    name = "Glosbe",
    autoLanguageCode = null,
    defaultUrl = "https://translator-api.glosbe.com/",
    urlModifiable = false,
    supportsSimTranslation = true
) {
    lateinit var api: Glosbe

    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return listOf(
            "ace" to "Achinese",
            "acm" to "Mesopotamian Arabic",
            "acq" to "Ta\'izzi-Adeni Arabic",
            "aeb" to "Tunisian Arabic",
            "af " to "Afrikaans",
            "ajp" to "South Levantine Arabic",
            "ak " to "Akan",
            "als" to "Tosk Albanian",
            "am " to "Amharic",
            "apc" to "North Levantine Arabic",
            "ar " to "Arabic",
            "ars" to "Najdi Arabic",
            "ary" to "Moroccan Arabic",
            "arz" to "Egyptian Arabic",
            "as " to "Assamese",
            "ast" to "Asturian",
            "awa" to "Awadhi",
            "ayr" to "Central Aymara",
            "az " to "Azerbaijani",
            "azb" to "South Azerbaijani",
            "ba " to "Bashkir",
            "ban" to "Balinese",
            "be " to "Belarusian",
            "bem" to "Bemba",
            "bg " to "Bulgarian",
            "bho" to "Bhojpuri",
            "bjn" to "Banjar",
            "bm " to "Bambara",
            "bn " to "Bangla",
            "bo " to "Tibetan",
            "bs " to "Bosnian",
            "bug" to "Buginese",
            "ca " to "Catalan",
            "ceb" to "Cebuano",
            "cjk" to "Chokwe",
            "ckb" to "Sorani Kurdish",
            "crh" to "Crimean Tatar",
            "cs " to "Czech",
            "cy " to "Welsh",
            "da " to "Danish",
            "de " to "German",
            "dik" to "Southwestern Dinka",
            "dyu" to "Jula",
            "dz " to "Dzongkha",
            "ee " to "Ewe",
            "el " to "Greek",
            "en " to "English",
            "eo " to "Esperanto",
            "es " to "Spanish",
            "et " to "Estonian",
            "eu " to "Basque",
            "fa " to "Persian",
            "fi " to "Finnish",
            "fj " to "Fijian",
            "fo " to "Faroese",
            "fon" to "Fon",
            "fr " to "French",
            "fur" to "Friulian",
            "fuv" to "Nigerian Fulfulde",
            "ga " to "Irish",
            "gaz" to "West Central Oromo",
            "gd " to "Scottish Gaelic",
            "gl " to "Galician",
            "gn " to "Guarani",
            "gu " to "Gujarati",
            "ha " to "Hausa",
            "he " to "Hebrew",
            "hi " to "Hindi",
            "hne" to "Chhattisgarhi",
            "hr " to "Croatian",
            "ht " to "Haitian",
            "hu " to "Hungarian",
            "hy " to "Armenian",
            "id " to "Indonesian",
            "ig " to "Igbo",
            "ilo" to "Iloko",
            "is " to "Icelandic",
            "it " to "Italian",
            "ja " to "Japanese",
            "jv " to "Javanese",
            "ka " to "Georgian",
            "kab" to "Kabyle",
            "kac" to "Kachin",
            "kam" to "Kamba",
            "kbp" to "Kabiyè",
            "kea" to "Kabuverdianu",
            "kg " to "Kongo",
            "ki " to "Kikuyu",
            "kk " to "Kazakh",
            "km " to "Khmer",
            "kmb" to "Kimbundu",
            "kn " to "Kannada",
            "knc" to "Central Kanuri",
            "ko " to "Korean",
            "ks " to "Kashmiri",
            "ku " to "Kurdish Kurmanji",
            "ky " to "Kyrgyz",
            "lb " to "Luxembourgish",
            "lg " to "Ganda",
            "li " to "Limburgish",
            "lij" to "Ligurian",
            "lmo" to "Lombard",
            "ln " to "Lingala",
            "lo " to "Lao",
            "lt " to "Lithuanian",
            "ltg" to "Latgalian",
            "lua" to "Luba-Lulua",
            "luo" to "Luo",
            "lus" to "Lushai",
            "lv " to "Latvian",
            "mag" to "Magahi",
            "mai" to "Maithili",
            "mi " to "Maori",
            "min" to "Minangkabau",
            "mk " to "Macedonian",
            "ml " to "Malayalam",
            "mn " to "Mongolian",
            "mni" to "Manipuri",
            "mos" to "Mòoré",
            "mr " to "Marathi",
            "ms " to "Malay",
            "mt " to "Maltese",
            "my " to "Burmese",
            "nb " to "Norwegian",
            "nl " to "Dutch",
            "nn " to "Norwegian Nynorsk",
            "npi" to "Nepali",
            "nso" to "Northern Sotho",
            "nus" to "Nuer",
            "ny " to "Nyanja",
            "oc " to "Occitan",
            "ory" to "Odia",
            "pa " to "Panjabi",
            "pag" to "Pangasinan",
            "pap" to "Papiamento",
            "pbt" to "Southern Pashto",
            "pl " to "Polish",
            "plt" to "Plateau Malagasy",
            "prs" to "Dari",
            "pt " to "Portuguese",
            "quy" to "Ayacucho Quechua",
            "rn " to "Rundi",
            "ro " to "Romanian",
            "ru " to "Russian",
            "rw " to "Kinyarwanda",
            "sa " to "Sanskrit",
            "sat" to "Santali",
            "sc " to "Sardinian",
            "scn" to "Sicilian",
            "sd " to "Sindhi",
            "sg " to "Sango",
            "shn" to "Shan",
            "si " to "Sinhala",
            "sk " to "Slovak",
            "sl " to "Slovenian",
            "sm " to "Samoan",
            "sn " to "Shona",
            "so " to "Somali",
            "sr " to "Serbian",
            "ss " to "Swati",
            "st " to "Southern Sotho",
            "su " to "Sundanese",
            "sv " to "Swedish",
            "sw " to "Swahili",
            "szl" to "Silesian",
            "ta " to "Tamil",
            "taq" to "Tamasheq",
            "te " to "Telugu",
            "tg " to "Tajik",
            "th " to "Thai",
            "ti " to "Tigrinya",
            "tk " to "Turkmen",
            "tl " to "Tagalog",
            "tn " to "Tswana",
            "tpi" to "Tok Pisin",
            "tr " to "Turkish",
            "ts " to "Tsonga",
            "tt " to "Tatar",
            "tum" to "Tumbuka",
            "tw " to "Twi",
            "tzm" to "Central Atlas Tamazight",
            "ug " to "Uighur",
            "uk " to "Ukrainian",
            "umb" to "Umbundu",
            "ur " to "Urdu",
            "uz " to "Uzbek",
            "vec" to "Venetian",
            "vi " to "Vietnamese",
            "war" to "Waray (Philippines)",
            "wo " to "Wolof",
            "xh " to "Xhosa",
            "ydd" to "Eastern Yiddish",
            "yo " to "Yoruba",
            "yue" to "Cantonese",
            "zh " to "Chinese",
            "zu " to "Zulu",
        ).map { Language(it.first, it.second) }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val translation = api.translate(source, target, query)
        return Translation(translation.translation)
    }
}
