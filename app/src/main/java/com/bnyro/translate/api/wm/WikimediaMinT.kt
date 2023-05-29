package com.bnyro.translate.api.wm

import com.bnyro.translate.api.wm.obj.WmTranslationRequest
import com.bnyro.translate.api.wm.obj.WmTranslationResponse
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WikimediaMinT {
    @GET("api/languages")
    suspend fun getLanguages(): JsonObject

    @POST("api/translate/{source}/{target}")
    suspend fun translate(
        @Path("source") source: String,
        @Path("target") target: String,
        @Body translationRequest: WmTranslationRequest
    ): WmTranslationResponse
}