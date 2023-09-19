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

package com.bnyro.translate.api.mm.obj

import kotlinx.serialization.Serializable

@Serializable
data class MMTranslationResponse(
    val exception_code: Int? = null,
    // returns a serialization error
    // val matches: List<MMTranslation>? = emptyList(),
    val mtLangSupported: Boolean? = false,
    val quotaFinished: Boolean? = false,
    val responderId: String? = "",
    val responseData: MMResponseData? = null,
    val responseDetails: String = "",
    val responseStatus: Int = 0
)
