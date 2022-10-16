package com.bnyro.translate.api.lt

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LTEngine : TranslationEngine(
    name = "LibreTranslate",
    defaultUrl = "https://libretranslate.de",
    urlModifiable = true,
    apiKeyState = ApiKeyState.OPTIONAL
) {

    private lateinit var api: LibreTranslate
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(
            this,
            LibreTranslate::class.java
        )
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().toMutableList().apply {
            add(
                0,
                Language(
                    code = "auto",
                    name = "Auto"
                )
            )
        }
    }

    override suspend fun translate(
        query: String,
        source: String,
        target: String
    ): String = api.translate(
        query,
        source,
        target,
        getApiKey()
    ).translatedText
}
