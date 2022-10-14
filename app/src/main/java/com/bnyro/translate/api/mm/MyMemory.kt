package com.bnyro.translate.api.mm

import com.bnyro.translate.api.mm.obj.MMTranslationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MyMemory {
    @GET("languages")
    suspend fun getLanguages(): Any

    @GET("get")
    suspend fun translate(
        @Query("q") query: String,
        @Query("langpair") langPair: String,
        @Query("key") key: String?
    ): MMTranslationResponse
}
