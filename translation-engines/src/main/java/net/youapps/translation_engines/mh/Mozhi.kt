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

package net.youapps.translation_engines.mh

import net.youapps.translation_engines.mh.obj.MhLanguage
import net.youapps.translation_engines.mh.obj.MhTranslationResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface Mozhi {
    @POST("api/translate/")
    @Multipart
    suspend fun translate(
        @Part("engine") engine: String? = null,
        @Part("from") source: String,
        @Part("to") target: String,
        @Part("text") query: String
    ): MhTranslationResponse

    @GET("api/target_languages/")
    suspend fun getLanguages(
        @Query("engine") engine: String?
    ): List<MhLanguage>

    @GET("api/tts")
    suspend fun getAudioFile(
        @Query("engine") engine: String? = "google",
        @Query("lang") lang: String,
        @Query("text") text: String
    ): Response<ResponseBody>
}
