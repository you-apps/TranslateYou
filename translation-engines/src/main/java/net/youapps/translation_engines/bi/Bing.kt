/*
 * Copyright (c) 2026 You Apps
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

package net.youapps.translation_engines.bi

import net.youapps.translation_engines.bi.obj.BingResponseItem
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private const val USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:159.0) Gecko/20100101 Firefox/159.0"

interface Bing {
    @GET("translator")
    @Headers("User-Agent: $USER_AGENT")
    suspend fun getTranslatorPage(): ResponseBody

    @POST("ttranslatev3")
    @FormUrlEncoded
    @Headers("User-Agent: $USER_AGENT")
    suspend fun translate(
        @Query("IG") ig: String,
        @Query("IID") iid: String,
        @Field("fromLang") fromLang: String,
        @Field("to") toLang: String,
        @Field("text") text: String,
        @Field("token") token: String,
        @Field("key") key: String,
    ): List<BingResponseItem>
}