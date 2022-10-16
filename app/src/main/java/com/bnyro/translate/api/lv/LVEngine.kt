package com.bnyro.translate.api.lv

import com.bnyro.translate.const.ApiKeyState
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitHelper
import com.bnyro.translate.util.TranslationEngine

class LVEngine : TranslationEngine(
    name = "Lingva",
    defaultUrl = "https://lingva.ml",
    urlModifiable = true,
    apiKeyState = ApiKeyState.DISABLED
) {

    private lateinit var api: LingvaTranslate
    override fun create(): TranslationEngine = apply {
        api = RetrofitHelper.createApi(
            this,
            LingvaTranslate::class.java
        )
    }

    override suspend fun getLanguages(): List<Language> {
        return api.getLanguages().languages
    }

    override suspend fun translate(query: String, source: String, target: String): String {
        return api.translate(
            source,
            target,
            query
        ).translation
    }
}
