package com.bnyro.translate.api.st

import com.bnyro.translate.api.st.obj.STTranslationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SimplyTranslate {
    @GET("api/translate/")
    suspend fun translate(
        @Query("engine") engine: String? = null,
        @Query("from") source: String,
        @Query("to") target: String,
        @Query("text") query: String
    ): STTranslationResponse

    @GET("api/target_languages/")
    suspend fun getLanguages(
        @Query("engine") engine: String?
    ): String
}
