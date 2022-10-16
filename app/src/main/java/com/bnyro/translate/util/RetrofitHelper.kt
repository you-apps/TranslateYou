package com.bnyro.translate.util

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitHelper {
    fun <T> createApi(engine: TranslationEngine, type: Class<T>): T {
        val baseUrl = engine.getUrl()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                JacksonConverterFactory.create()
            )
            .build()
            .create(type)
    }
}
