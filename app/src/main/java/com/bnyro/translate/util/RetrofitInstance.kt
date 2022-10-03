package com.bnyro.translate.util

import com.bnyro.translate.api.lt.LibreTranslate
import com.bnyro.translate.api.lv.LingvaTranslate
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.create

object RetrofitInstance {
    lateinit var libreTranslate: LibreTranslate
    lateinit var lingvaTranslate: LingvaTranslate

    fun createApi() {
        val url = Preferences.get(
            Preferences.instanceUrlKey,
            Preferences.defaultInstanceUrl
        )

        val builder = Retrofit.Builder()
            .baseUrl("https://lingva.ml")
            .addConverterFactory(
                JacksonConverterFactory.create()
            )
            .build()

        libreTranslate = builder.create(LibreTranslate::class.java)

        lingvaTranslate = builder.create(LingvaTranslate::class.java)
    }
}
