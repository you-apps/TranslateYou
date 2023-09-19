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
