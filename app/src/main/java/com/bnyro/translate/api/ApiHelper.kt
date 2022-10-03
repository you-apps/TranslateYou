package com.bnyro.translate.api

import com.bnyro.translate.obj.Language

abstract class ApiHelper {
    abstract suspend fun getLanguages(): List<Language>
    abstract suspend fun translate(query: String, source: String, target: String): String
}
