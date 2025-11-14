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

package com.bnyro.translate.api.ya

import com.bnyro.translate.api.ya.obj.YandexResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface Yandex {
    @POST("/api/v1/tr.json/translate")
    suspend fun translate(
        @Query("lang") language: String,
        @Query("text") text: String,
        @Query("srv") client: String,
        @Query("sid") clientId: String
    ): YandexResponse

    @GET()
    suspend fun getMainPage(
        @Url url: String = "https://translate.yandex.com" // not .net like the API
    ): String
}