/*
 * Copyright (c) 2025 You Apps
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.bnyro.translate.api.kagi

import com.bnyro.translate.api.kagi.obj.KagiLanguage
import com.bnyro.translate.api.kagi.obj.KagiTranslationRequest
import com.bnyro.translate.api.kagi.obj.KagiTranslationResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Kagi {
    @POST("api/translate")
    suspend fun translate(
        @Query("token") token: String,
        @Body request: KagiTranslationRequest
    ): KagiTranslationResponse
    
    @GET("api/list-languages")
    suspend fun getLanguages(): List<KagiLanguage>
} 