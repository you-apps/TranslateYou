package com.bnyro.translate.util

import com.bnyro.translate.api.APIHelper
import java.net.URL

abstract class TranslationEngine(
    val name: String,
    val defaultUrl: String,
    val urlModifiable: Boolean,
    val apiKeyState: Int
) {
    lateinit var apiHelper: APIHelper

    abstract fun create(): TranslationEngine

    val urlPrefKey = this.name + Preferences.instanceUrlKey
    val apiPrefKey = this.name + Preferences.apiKey
    fun getUrl(): String {
        val url = Preferences.get(
            urlPrefKey,
            this.defaultUrl
        )

        return try {
            URL(url)
            url
        } catch (e: Exception) {
            defaultUrl
        }
    }

    fun getApiKey() = Preferences.get(
        apiPrefKey,
        ""
    )
}
