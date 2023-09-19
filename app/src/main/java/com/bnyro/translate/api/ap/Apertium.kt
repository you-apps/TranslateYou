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

package com.bnyro.translate.api.ap

import com.bnyro.translate.api.ap.obj.ApertiumResponse
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apertium {
    @Multipart
    @POST("apy/translate")
    suspend fun translate(
        // language codes, separated by "|"
        @Part("langpair") langPair: String,
        @Part("q") query: String,
        @Part("markUnknown") markUnknown: String = "no",
        @Part("prefs") prefs: String = "",
    ): ApertiumResponse
}