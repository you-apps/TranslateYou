package com.bnyro.translate.api.deepl

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.db.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class DeeplEngine : TranslationEngine(
    name = "DeepL",
    defaultUrl = "https://api-free.deepl.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.REQUIRED,
    autoLanguageCode = ""
) {
    val useFreeApiKey = this.name + "selectedApiVersion"
    private val nonFreeUrl = "https://api.deepl.com"
    private lateinit var api: DeepL
    override fun createOrRecreate(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(this)
    }

    private val apiKeyString = "DeepL-Auth-Key " + getApiKey()

    override suspend fun getLanguages(): List<Language> = api.getLanguages(
        apiKeyString
    ).map {
        Language(
            code = it.language.lowercase(),
            name = it.name
        )
    }.toMutableList().apply {
        add(
            0,
            Language(
                "",
                "Auto"
            )
        )
    }

    override suspend fun translate(query: String, source: String, target: String): Translation {
        val response = api.translate(
            apiKeyString,
            sourceOrAuto(source.uppercase()),
            target.uppercase(),
            query
        )

        return Translation(
            translatedText = response.translations.first().text,
            detectedLanguage = response.translations.first().detected_source_language
        )
    }

    override fun getUrl() = if (Preferences.get(useFreeApiKey, true)) defaultUrl else nonFreeUrl
}
