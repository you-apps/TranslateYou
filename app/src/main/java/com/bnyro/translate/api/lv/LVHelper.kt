package com.bnyro.translate.api.lv

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.RetrofitInstance

class LVHelper : APIHelper() {
    override suspend fun getLanguages(): List<Language> {
        return RetrofitInstance.lingvaTranslate.getLanguages().languages
    }

    override suspend fun translate(query: String, source: String, target: String): String {
        return RetrofitInstance.lingvaTranslate.translate(
            source,
            target,
            query
        ).translation
    }
}
