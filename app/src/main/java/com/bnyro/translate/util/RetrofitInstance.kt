package com.bnyro.translate.util

import com.bnyro.translate.api.LibreTranslate
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitInstance {
    var url = "https://libretranslate.de"

    lateinit var api: LibreTranslate

    fun createApi() {
        api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                JacksonConverterFactory.create()
            )
            .build()
            .create(LibreTranslate::class.java)
    }
}
