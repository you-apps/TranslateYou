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
            "af" to "Afrikaans",
            "sq" to "Albanian",
            "ar" to "Arabic",
            "hy" to "Armenian",
            "az" to "Azerbaijani",
            "bn" to "Bangla",
            "eu" to "Basque",
            "be" to "Belarusian",
            "bs" to "Bosnian",
            "bg" to "Bulgarian",
            "my" to "Burmese",
            "ca" to "Catalan",
            "zh" to "Chinese",
            "hr" to "Croatian",
            "cs" to "Czech",
            "da" to "Danish",
            "nl" to "Dutch",
            "en" to "English",
            "eo" to "Esperanto",
            "et" to "Estonian",
            "fil" to "Filipino",
            "fi" to "Finnish",
            "fr" to "French",
            "gl" to "Galician",
            "ka" to "Georgian",
            "de" to "German",
            "el" to "Greek",
            "gu" to "Gujarati",
            "ht" to "Haitian",
            "he" to "Hebrew",
            "hi" to "Hindi",
            "hu" to "Hungarian",
            "io" to "Ido",
            "id" to "Indonesian",
            "ia" to "Interlingua",
            "ga" to "Irish",
            "it" to "Italian",
            "ja" to "Japanese",
            "kk" to "Kazakh",
            "km" to "Khmer",
            "ko" to "Korean",
            "ku" to "Kurdish Kurmanji",
            "lo" to "Lao",
            "la" to "Latin",
            "lv" to "Latvian",
            "lt" to "Lithuanian",
            "nds" to "Low German",
            "mk" to "Macedonian",
            "ms" to "Malay",
            "ml" to "Malayalam",
            "mt" to "Maltese",
            "mr" to "Marathi",
            "mn" to "Mongolian",
            "nv" to "Navajo",
            "nap" to "Neapolitan",
            "nb" to "Norwegian",
            "fa" to "Persian",
            "pl" to "Polish",
            "pt" to "Portuguese",
            "ro" to "Romanian",
            "rom" to "Romany",
            "ru" to "Russian",
            "sa" to "Sanskrit",
            "sr" to "Serbian",
            "sk" to "Slovak",
            "sl" to "Slovenian",
            "es" to "Spanish",
            "sw" to "Swahili",
            "sv" to "Swedish",
            "tl" to "Tagalog",
            "tg" to "Tajik",
            "ta" to "Tamil",
            "tt" to "Tatar",
            "te" to "Telugu",
            "th" to "Thai",
            "tr" to "Turkish",
            "tk" to "Turkmen",
            "uk" to "Ukrainian",
            "ur" to "Urdu",
            "uz" to "Uzbek",
            "vi" to "Vietnamese",
            "vo" to "Volapük",
            "yo" to "Yoruba",
            "zu" to "Zulu",
        ).map { Language(it.first, it.second) }
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val translation = api.translate(source, target, query)
        return Translation(translation.translation)
    }
}