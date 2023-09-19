/*
 * Copyright (c) 2023 You Apps
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