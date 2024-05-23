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

package com.bnyro.translate.api.mh.obj

import com.bnyro.translate.api.st.obj.STDefinition
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MhTranslationResponse (
    @SerialName("definitions")
    val definitions: STDefinition? = null,
    @SerialName("pronunciation")
    val pronunciation: String? = null,
    @SerialName("detected")
    val detectedLanguage: String? = null,
    @SerialName("translated-text")
    val translatedText: String = ""
)
