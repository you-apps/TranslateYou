package com.bnyro.translate.api.lv

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.obj.Language

class LVHelper(
    private val api: LingvaTranslate
) : APIHelper() {
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
