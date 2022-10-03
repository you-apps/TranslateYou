package com.bnyro.translate.api

import com.bnyro.translate.obj.Language
import com.bnyro.translate.obj.Translation

abstract class ApiHelper {
    abstract suspend fun getLanguages(): List<Language>
    abstract suspend fun translate(query: String, source: String, target: String): Translation
}
