package com.bnyro.translate.api.lv

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.constants.TranslationEngines
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitHelper

class LVHelper() : APIHelper() {
    private val api: LingvaTranslate = RetrofitHelper.createApi(
        TranslationEngines.lingvaTranslate,
        LingvaTranslate::class.java
    )

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
