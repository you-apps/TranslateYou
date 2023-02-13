package com.bnyro.translate.util

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType
import retrofit2.Retrofit

object RetrofitHelper {
    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> createApi(engine: TranslationEngine): T {
        val baseUrl = engine.getUrl()

        val contentType = MediaType.parse("application/json")!!

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JsonHelper.json.asConverterFactory(contentType))
            .build()
            .create(T::class.java)
    }
}
