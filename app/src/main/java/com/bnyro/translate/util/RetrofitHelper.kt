package com.bnyro.translate.util

import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitHelper {
    fun <T> createApi(baseUrl: String, type: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                JacksonConverterFactory.create()
            )
            .build()
            .create(type)
    }
}
