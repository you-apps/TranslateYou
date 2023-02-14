package com.bnyro.translate.api.mm

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class MMEngine : TranslationEngine(
    name = "MyMemory",
    defaultUrl = "https://api.mymemory.translated.net",
    urlModifiable = false,
    apiKeyState = ApiKeyState.OPTIONAL,
    autoLanguageCode = "Autodetect"
) {
    lateinit var api: MyMemory
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    override suspend fun getLanguages(): List<Language> {
        return listOf(
            "am-ET" to "Amharic",
            "ar-SA" to "Arabic",
            "be-BY" to "Bielarus",
            "bem-ZM" to "Bemba",
            "bi-VU" to "Bislama",
            "bjs-BB" to "Bajan",
            "bn-IN" to "Bengali",
            "bo-CN" to "Tibetan",
            "br-FR" to "Breton",
            "bs-BA" to "Bosnian",
            "ca-ES" to "Catalan",
            "cop-EG" to "Coptic",
            "cs-CZ" to "Czech",
            "cy-GB" to "Welsh",
            "da-DK" to "Danish",
            "dz-BT" to "Dzongkha",
            "de-DE" to "German",
            "dv-MV" to "Maldivian",
            "el-GR" to "Greek",
            "en-GB" to "English",
            "es-ES" to "Spanish",
            "et-EE" to "Estonian",
            "eu-ES" to "Basque",
            "fa-IR" to "Persian",
            "fi-FI" to "Finnish",
            "fn-FNG" to "Fanagalo",
            "fo-FO" to "Faroese",
            "fr-FR" to "French",
            "gl-ES" to "Galician",
            "gu-IN" to "Gujarati",
            "ha-NE" to "Hausa",
            "he-IL" to "Hebrew",
            "hi-IN" to "Hindi",
            "hr-HR" to "Croatian",
            "hu-HU" to "Hungarian",
            "id-ID" to "Indonesian",
            "is-IS" to "Icelandic",
            "it-IT" to "Italian",
            "ja-JP" to "Japanese",
            "kk-KZ" to "Kazakh",
            "km-KM" to "Khmer",
            "kn-IN" to "Kannada",
            "ko-KR" to "Korean",
            "ku-TR" to "Kurdish",
            "ky-KG" to "Kyrgyz",
            "la-VA" to "Latin",
            "lo-LA" to "Lao",
            "lv-LV" to "Latvian",
            "men-SL" to "Mende",
            "mg-MG" to "Malagasy",
            "mi-NZ" to "Maori",
            "ms-MY" to "Malay",
            "mt-MT" to "Maltese",
            "my-MM" to "Burmese",
            "ne-NP" to "Nepali",
            "niu-NU" to "Niuean",
            "nl-NL" to "Dutch",
            "no-NO" to "Norwegian",
            "ny-MW" to "Nyanja",
            "ur-PK" to "Pakistani",
            "pau-PW" to "Palauan",
            "pa-IN" to "Panjabi",
            "ps-PK" to "Pashto",
            "pis-SB" to "Pijin",
            "pl-PL" to "Polish",
            "pt-PT" to "Portuguese",
            "rn-BI" to "Kirundi",
            "ro-RO" to "Romanian",
            "ru-RU" to "Russian",
            "sg-CF" to "Sango",
            "si-LK" to "Sinhala",
            "sk-SK" to "Slovak",
            "sm-WS" to "Samoan",
            "sn-ZW" to "Shona",
            "so-SO" to "Somali",
            "sq-AL" to "Albanian",
            "sr-RS" to "Serbian",
            "sv-SE" to "Swedish",
            "sw-SZ" to "Swahili",
            "ta-LK" to "Tamil",
            "te-IN" to "Telugu",
            "tet-TL" to "Tetum",
            "tg-TJ" to "Tajik",
            "th-TH" to "Thai",
            "ti-TI" to "Tigrinya",
            "tk-TM" to "Turkmen",
            "tl-PH" to "Tagalog",
            "tn-BW" to "Tswana",
            "to-TO" to "Tongan",
            "tr-TR" to "Turkish",
            "uk-UA" to "Ukrainian",
            "uz-UZ" to "Uzbek",
            "vi-VN" to "Vietnamese",
            "wo-SN" to "Wolof",
            "xh-ZA" to "Xhosa",
            "yi-YD" to "Yiddish",
            "zu-ZA" to "Zulu"
        )
            .map { Language(it.first, it.second) }
            .sortedBy { it.name }

        /* DEPRECATED endpoint apparently
        val request = Request.Builder()
            .url("$defaultUrl/languages")
            .build()

        val call = OkHttpClient().newCall(request)

        val json = awaitQuery {
            call.execute().body()!!.string()
        }

        val el = JsonHelper.json.parseToJsonElement(
            JsonHelper.json.encodeToString(json)
        )

        return el.jsonObject.entries.map {
            val code = it.value.jsonObject["c"].toString()
            Language(
                name = it.key,
                code = code
            )
        }
        */
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val key = getApiKey()
        val response = api.translate(
            query,
            "${sourceOrAuto(source)}|$target",
            if (key == "") null else key

        )
        return Translation(
            translatedText = response.responseData?.translatedText ?: "",
            detectedLanguage = response.responseData?.detectedLanguage
        )
    }
}
