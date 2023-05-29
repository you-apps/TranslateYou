package com.bnyro.translate.api.gl

import com.bnyro.translate.api.gl.obj.GlTranslationResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface Globe {
    @POST("translateByLangDetect")
    suspend fun translate(
        @Query("sourceLang") source: String,
        @Query("targetLang") target: String,
        @Body text: String
    ): GlTranslationResponse
}