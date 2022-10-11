package com.bnyro.translate.api.deepl

import com.bnyro.translate.api.deepl.obj.DeeplLanguage
import com.bnyro.translate.api.deepl.obj.DeeplTranslationResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DeepL {
    @GET("v2/languages")
    suspend fun getLanguages(
        @Header("Authorization") apiKeyString: String
    ): List<DeeplLanguage>

    @POST("v2/translate")
    suspend fun translate(
        @Header("Authorization") apiKeyString: String,
        @Query("source_lang") source: String,
        @Query("target_lang") target: String,
        @Query("text") query: String
    ): DeeplTranslationResponse
}
