package com.bnyro.translate.api.lt

import com.bnyro.translate.api.ApiHelper
import com.bnyro.translate.obj.Language
import com.bnyro.translate.obj.Translation
import com.bnyro.translate.util.Preferences
import com.bnyro.translate.util.RetrofitInstance

class LTHelper : ApiHelper() {
    override suspend fun getLanguages(): List<Language> = RetrofitInstance.api.getLanguages()

    override suspend fun translate(
        query: String,
        source: String,
        target: String
    ): Translation = RetrofitInstance.api.translate(
        query,
        source,
        target,
        Preferences.get(Preferences.apiKey, "")
    )
}
