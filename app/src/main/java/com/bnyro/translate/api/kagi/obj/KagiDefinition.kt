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

package com.bnyro.translate.api.kagi.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KagiDefinition(
    val word: String = "",
    val language: String = "",
    @SerialName("part_of_speech") val partOfSpeech: List<String> = emptyList(),
    @SerialName("usage_level") val usageLevel: List<String> = emptyList(),
    @SerialName("primary_meaning") val primaryMeaning: KagiMeaning? = null,
    @SerialName("secondary_meanings") val secondaryMeanings: List<KagiMeaning> = emptyList(),
    val examples: List<String> = emptyList(),
    val pronunciation: String? = null,
    val etymology: String? = null,
    @SerialName("usage_notes") val usageNotes: List<String> = emptyList(),
    @SerialName("image_prompt") val imagePrompt: String? = null
)

@Serializable
data class KagiMeaning(
    val definition: String = "",
    @SerialName("part_of_speech") val partOfSpeech: List<String> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val dialect: List<String> = emptyList(),
    @SerialName("usage_note") val usageNote: String? = null,
    @SerialName("usage_level") val usageLevel: List<String> = emptyList()
) 