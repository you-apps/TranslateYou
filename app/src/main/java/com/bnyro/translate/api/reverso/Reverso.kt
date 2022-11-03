package com.bnyro.translate.api.reverso

import com.bnyro.translate.api.reverso.obj.ReversoRequestBody
import com.bnyro.translate.api.reverso.obj.ReversoTranslationResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Reverso {
    @POST("translate/v1/translation")
    suspend fun translate(
        @Header("User-Agent") userAgent: String = "Mozilla/5.0",
        @Body requestBody: ReversoRequestBody
    ): ReversoTranslationResponse
}
