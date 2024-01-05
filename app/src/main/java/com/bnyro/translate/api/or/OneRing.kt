/*
 * Copyright (c) 2024 You Apps
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

package com.bnyro.translate.api.or

import com.bnyro.translate.api.or.obj.OneRingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OneRing {
     @GET("translate")
     suspend fun translate(
         @Query("text") text: String,
         @Query("from_lang") from: String,
         @Query("to") to: String,
         @Query("translator_plugin") plugin: String,
         @Query("api_key") apiKey: String?
     ): OneRingResponse
}