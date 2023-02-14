package com.bnyro.translate.util

import com.bnyro.translate.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitHelper {
    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> createApi(engine: TranslationEngine): T {
        val baseUrl = engine.getUrl()

        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            httpClient.addInterceptor(logging) // <-- this is the important line!
        }

        val contentType = "application/json".toMediaTypeOrNull()!!

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JsonHelper.json.asConverterFactory(contentType))
            .client(httpClient.build())
            .build()
            .create(T::class.java)
    }
}
