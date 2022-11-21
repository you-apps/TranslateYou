package com.bnyro.translate.api.lt

import com.bnyro.translate.api.lt.obj.LTTranslation
import com.bnyro.translate.db.obj.Language
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LibreTranslate {
    @GET("languages")
    suspend fun getLanguages(): List<Language>

    @POST("translate")
    suspend fun translate(
        @Query("q") query: String,
        @Query("source") source: String,
        @Query("target") target: String,
        @Query("api_key") apikey: String?
    ): LTTranslation
}
