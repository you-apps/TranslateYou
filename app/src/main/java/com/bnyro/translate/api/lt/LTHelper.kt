package com.bnyro.translate.api.lt

import com.bnyro.translate.api.APIHelper
import com.bnyro.translate.obj.Language
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitInstance

class LTHelper : APIHelper() {
    override suspend fun getLanguages(): List<Language> {
        return RetrofitInstance.libreTranslate.getLanguages().toMutableList().apply {
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
    ): String = RetrofitInstance.libreTranslate.translate(
        query,
        source,
        target,
        Preferences.get(Preferences.apiKey, "")
    ).translatedText
}
