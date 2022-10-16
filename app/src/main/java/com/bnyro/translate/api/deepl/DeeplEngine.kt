package com.bnyro.translate.api.deepl

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class DeeplEngine : TranslationEngine(
    name = "DeepL",
    defaultUrl = "https://api-free.deepl.com",
    urlModifiable = false,
    apiKeyState = ApiKeyState.REQUIRED
) {

    private lateinit var api: DeepL
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(
            this,
            DeepL::class.java
        )
    }

    private val apiKeyString = "DeepL-Auth-Key " + getApiKey()

    override suspend fun getLanguages(): List<Language> = api.getLanguages(
        apiKeyString
    ).map {
        Language(
            code = it.language,
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

    override suspend fun translate(query: String, source: String, target: String): String =
        api.translate(
            apiKeyString,
            source,
            target,
            query
        ).translations[0].text
}
