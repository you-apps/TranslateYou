package com.bnyro.translate.util

import com.bnyro.translate.api.APIHelper

open class TranslationEngine(
    val id: Int,
    val name: String,
    val defaultUrl: String,
    val urlModifiable: Boolean,
    val apiKeyState: Int
) {
    lateinit var apiHelper: APIHelper

    fun <T> createApi(type: Class<T>): T {
        return RetrofitHelper.createApi(
            Preferences.get(
                name + Preferences.instanceUrlKey,
                defaultUrl
            ),
            type
        )
    }
}
