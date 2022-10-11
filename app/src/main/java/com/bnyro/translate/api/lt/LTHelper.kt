package com.bnyro.translate.api.lt

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.constants.TranslationEngines
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitHelper

class LTHelper() : APIHelper() {
    private val api: LibreTranslate = RetrofitHelper.createApi(
        TranslationEngines.libreTranslate,
        LibreTranslate::class.java
    )
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
        Preferences.getApiKeyByEngine(
            TranslationEngines.libreTranslate
        )
    ).translatedText
}
