package com.bnyro.translate.util

import com.bnyro.translate.api.lt.LibreTranslate
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {
    lateinit var api: LibreTranslate

    fun createApi() {
        val url = Preferences.get(
            Preferences.instanceUrlKey,
            Preferences.defaultInstanceUrl
        )

        api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                JacksonConverterFactory.create()
            )
            .build()
            .create(LibreTranslate::class.java)
    }
}
