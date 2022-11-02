package com.bnyro.translate.api.reverso

import com.bnyro.translate.api.reverso.obj.ReversoRequestBody
import com.bnyro.translate.api.reverso.obj.ReversoTranslationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Reverso {
    @POST("translate/v1/translation")
    suspend fun translate(
        @Body requestBody: ReversoRequestBody
    ): ReversoTranslationResponse
}
