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

package net.youapps.translation_engines.iciba

import kotlinx.serialization.json.JsonObject
import net.youapps.translation_engines.iciba.obj.IcibaRequest
import net.youapps.translation_engines.iciba.obj.IcibaResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Iciba {
    // https://codeberg.org/ManeraKai/simplytranslate/src/branch/main/engines/iciba.go#L237
    @POST("dictionary/fy/batch")
    suspend fun translate(
        @Query("client") client: Int = 6,
        @Query("key") key: String = "1000006",
        @Query("timestamp") timestamp: String,
        @Query("signature") signature: String,
        @Body request: IcibaRequest,
    ): IcibaResponse

    @GET("https://ifanyi.iciba.com/index.php")
    suspend fun getLanguages(
        @Query("c") c: String = "trans",
        @Query("m") m: String = "getLanguage",
        @Query("q") q: Int = 0,
        @Query("type") type: String = "en"
    ): Map<String, JsonObject>
}